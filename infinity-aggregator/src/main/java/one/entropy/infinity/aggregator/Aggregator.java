package one.entropy.infinity.aggregator;

import com.datastax.spark.connector.CassandraSparkExtensions;
import org.apache.camel.Exchange;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StringType;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                .config("spark.cassandra.connection.host", host)
                .config("spark.cassandra.connection.port", "9042")
                .config("spark.sql.catalog.casscatalog", "com.datastax.spark.connector.datasource.CassandraCatalog")
                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.host", host)
                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.port", "9042")
                .getOrCreate();
        tableProperties.put("keyspace", "infinity_ks");
        tableProperties.put("table", "aggregations");
        tableProperties.put("output.consistency.level", "ONE");
    }

    protected void process(Exchange exchange) throws Exception {
        AggregationRequest request = exchange.getIn().getBody(AggregationRequest.class);
        LOGGER.log(Level.INFO, "Process request: {0}", request);
        try {
            aggregate(request.getEventGroup(), request.getEventType());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        } catch (AssertionError a) {
            LOGGER.info(a.getMessage());
            throw new Exception(a.getMessage());
        }
    }

    private void aggregate(String eventGroup, String eventType) {
        LOGGER.log(Level.INFO, "Aggregate events for eventGroup: {0} eventType: {1}", new String[]{eventGroup, eventType});
        Dataset<Row> dataset = selectEvents(eventGroup, eventType);
        Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS).forEach(horizon -> {
            Dataset<Row> aggregation = aggregateEvents(dataset, horizon);
            saveAggregations(aggregation);
        });
    }

    private Dataset<Row> selectEvents(String eventGroup, String eventType) {
        LOGGER.log(Level.INFO, "Select events for eventGroup: {0} eventType: {1}", new String[]{eventGroup, eventType});
        Dataset<Row> dataset = spark.sql("select * from casscatalog.infinity_ks.events_by_time;")
                .where(new Column("event_group").equalTo(eventGroup))
                .where(new Column("event_type").equalTo(eventType));
        return dataset;
    }

    private Dataset<Row> aggregateEvents(Dataset<Row> dataset, ChronoUnit horizon) {
        LOGGER.log(Level.INFO, "Aggregate events for {0}", horizon);

        Column[] groupBy = createGroupBy(horizon);
        Map<String, Column> withColumns = createWithColumn(horizon);

        Dataset<Row> agg = dataset.groupBy(groupBy).agg(
                functions.avg("value").alias("avg_value"),
                functions.min("value").alias("min_value"),
                functions.max("value").alias("max_value"),
                functions.mean("value").alias("mean_value"),
                functions.count("value").alias("count_value"),
                functions.sum("value").alias("sum_value"));

        for (Map.Entry<String, Column> entry : withColumns.entrySet()) {
            agg = agg.withColumn(entry.getKey(), entry.getValue());
        }

        agg = agg.drop("event_year").drop("event_month").drop("event_day").drop("event_hour").drop("event_minute").drop("event_second");

        agg.show();
        return agg;
    }

    private Map<String, Column> createWithColumn(ChronoUnit horizon) {
        Map<String, Column> withColumn = new HashMap<>();
        withColumn.put("horizon", functions.lit(horizon.name()));

        if (horizon.equals(ChronoUnit.YEARS)) {
            withColumn.put("period", new Column("event_year").cast(DataTypes.StringType));
        } else  if (horizon.equals(ChronoUnit.MONTHS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.DAYS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.HOURS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.MINUTES)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_minute").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.SECONDS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_minute").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_second").cast(DataTypes.StringType), 2, "0")
            ));
        }
        return withColumn;
    }

    private Column[] createGroupBy(ChronoUnit horizon) {
        List<Column> groupBy = new ArrayList<>();
        groupBy.add(new Column("event_group"));
        groupBy.add(new Column("event_type"));
        groupBy.add(new Column("event_year"));
        if (horizon.equals(ChronoUnit.MONTHS)) {
            groupBy.add(new Column("event_month"));
        } else if (horizon.equals(ChronoUnit.DAYS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
        } else if (horizon.equals(ChronoUnit.HOURS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
        } else if (horizon.equals(ChronoUnit.MINUTES)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
            groupBy.add(new Column("event_minute"));
        } else if (horizon.equals(ChronoUnit.SECONDS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
            groupBy.add(new Column("event_minute"));
            groupBy.add(new Column("event_second"));
        }
        return groupBy.toArray(new Column[groupBy.size()]);
    }

    private void saveAggregations(Dataset<Row> dataset) {
        LOGGER.log(Level.INFO, "Save aggregations");
        dataset.write().format("org.apache.spark.sql.cassandra").options(tableProperties).mode(SaveMode.Append).save();
    }
}
