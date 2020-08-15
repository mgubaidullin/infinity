package one.entropy.infinity.aggregator;

import com.datastax.spark.connector.CassandraSparkExtensions;
import org.apache.camel.Exchange;
import org.apache.spark.sql.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Aggregator {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private SparkSession spark;
    private Map<String, String> tableProperties = new HashMap();

    public Aggregator(String host) {
        LOGGER.log(Level.INFO, "Creating SparkSession connected to: {0}", host);
        spark = SparkSession.builder()
                .appName("Infinity Aggregator")
                .master("local")
                .withExtensions(new CassandraSparkExtensions())
                .config("spark.sql.catalog.casscatalog", "com.datastax.spark.connector.datasource.CassandraCatalog")
                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.host", host)
                .getOrCreate();
        tableProperties.put("keyspace", "infinity_ks");
        tableProperties.put("table", "aggregations");
//        tableProperties.put("confirm.truncate", "true");
    }

    protected void process(Exchange exchange) throws Exception {
        AggregatorCommand command = exchange.getIn().getBody(AggregatorCommand.class);
        ChronoUnit horizon = command.getHorizon();
        LOGGER.log(Level.INFO, "Process command: {0}", command);
        try {
            final ZonedDateTime timestamp = command.getTimestamp().atZone(ZoneOffset.UTC);

            if (horizon.equals(ChronoUnit.SECONDS)) {
                IntStream.range(1, 10).forEach(i -> {
                    ZonedDateTime t = timestamp.minus(i, horizon);
                    aggregate(horizon, t);
                });
            } else {
                ZonedDateTime t = timestamp.minus(1, horizon);
                aggregate(horizon, t);
            }


        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (AssertionError a) {
            LOGGER.info(a.getMessage());
            throw new Exception(a.getMessage());
        }
    }

    private void aggregate(ChronoUnit horizon, ZonedDateTime timestamp){
        LOGGER.log(Level.INFO, "Aggregate horizon: {0} at {1}", new String[]{horizon.toString(), timestamp.toString()});
        Dataset<Row> dataset = selectEvents(horizon, timestamp);
        Dataset<Row> avg = aggregateEvents(dataset, horizon, timestamp, functions.avg("value").alias("value"), "AVG");
        Dataset<Row> min = aggregateEvents(dataset, horizon, timestamp, functions.min("value").alias("value"), "MIN");
        Dataset<Row> max = aggregateEvents(dataset, horizon, timestamp, functions.max("value").alias("value"), "MAX");
        Dataset<Row> mean = aggregateEvents(dataset, horizon, timestamp, functions.mean("value").alias("value"), "MEAN");
        Dataset<Row> count = aggregateEvents(dataset, horizon, timestamp, functions.count("value").alias("value"), "COUNT");

        saveAggregations(avg);
        saveAggregations(min);
        saveAggregations(max);
        saveAggregations(mean);
        saveAggregations(count);
    }

    private Dataset<Row> selectEvents(ChronoUnit horizon, ZonedDateTime timestamp){
        LOGGER.log(Level.INFO, "Select events for horizon: {0} at {1}", new String[]{horizon.toString(), timestamp.toString()});
        Dataset<Row> dataset = spark.sql("select * from casscatalog.infinity_ks.events_by_time;")
                .where(new Column("event_year").equalTo(timestamp.getYear()));

        if (horizon.equals(ChronoUnit.MONTHS)) {
            dataset = dataset.where(new Column("event_month").equalTo(timestamp.getMonthValue()))
                    .where(new Column("event_day").between(1, 31))
                    .where(new Column("event_hour").between(0, 23))
                    .where(new Column("event_minute").between(0, 59))
                    .where(new Column("event_second").between(0, 59));
        } else if (horizon.equals(ChronoUnit.DAYS)) {
            dataset = dataset.where(new Column("event_month").equalTo(timestamp.getMonthValue()))
                    .where(new Column("event_day").equalTo(timestamp.getDayOfMonth()))
                    .where(new Column("event_hour").between(0, 23))
                    .where(new Column("event_minute").between(0, 59))
                    .where(new Column("event_second").between(0, 59));
        } else if (horizon.equals(ChronoUnit.HOURS)) {
            dataset = dataset.where(new Column("event_month").equalTo(timestamp.getMonthValue()))
                    .where(new Column("event_day").equalTo(timestamp.getDayOfMonth()))
                    .where(new Column("event_hour").equalTo(timestamp.getHour()))
                    .where(new Column("event_minute").between(0, 59))
                    .where(new Column("event_second").between(0, 59));
        } else if (horizon.equals(ChronoUnit.MINUTES)) {
            dataset = dataset.where(new Column("event_month").equalTo(timestamp.getMonthValue()))
                    .where(new Column("event_day").equalTo(timestamp.getDayOfMonth()))
                    .where(new Column("event_hour").equalTo(timestamp.getHour()))
                    .where(new Column("event_minute").equalTo(timestamp.getMinute()))
                    .where(new Column("event_second").between(0, 59));
        } else if (horizon.equals(ChronoUnit.SECONDS)) {
            dataset = dataset.where(new Column("event_month").equalTo(timestamp.getMonthValue()))
                    .where(new Column("event_day").equalTo(timestamp.getDayOfMonth()))
                    .where(new Column("event_hour").equalTo(timestamp.getHour()))
                    .where(new Column("event_minute").equalTo(timestamp.getMinute()))
                    .where(new Column("event_second").equalTo(timestamp.getSecond()));
        }
        return dataset;
    }

    private Dataset<Row> aggregateEvents(Dataset<Row> dataset, ChronoUnit horizon, ZonedDateTime timestamp, Column column, String aggType){
        LOGGER.log(Level.INFO, "Aggregate events for horizon: {0} type: {1} at {2}", new String[]{horizon.toString(), aggType, timestamp.toString()});
        Dataset<Row> avg = dataset.groupBy("event_group", "event_type").agg(column)
                .withColumn("agg_type", functions.lit(aggType))
                .withColumn("horizon", functions.lit(horizon.toString()))
                .withColumn("agg_year", functions.lit(timestamp.getYear()))
                .withColumn("agg_month", functions.lit(timestamp.getMonthValue()))
                .withColumn("agg_day", functions.lit(timestamp.getDayOfMonth()))
                .withColumn("agg_hour", functions.lit(timestamp.getHour()))
                .withColumn("agg_minute", functions.lit(timestamp.getMinute()))
                .withColumn("agg_second", functions.lit(timestamp.getSecond()));
        avg.show();
        return avg;
    }

    private void saveAggregations(Dataset<Row> dataset){
        LOGGER.log(Level.INFO, "Save aggregations");
        dataset.write().format("org.apache.spark.sql.cassandra").options(tableProperties).mode(SaveMode.Append).save();
    }
}
