package one.entropy.infinity.aggregator;

import com.datastax.spark.connector.CassandraSparkExtensions;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());
    static SparkSession spark;

//    static {
//        spark = SparkSession.builder()
//                .appName("Infinity Aggregator")
//                .master("local")
//                .withExtensions(new CassandraSparkExtensions())
//                .config("spark.sql.catalog.casscatalog", "com.datastax.spark.connector.datasource.CassandraCatalog")
//                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.host", "localhost")
//                .getOrCreate();
//    }

    public static void main(String[] args) throws Exception {

        Column c = functions.avg("value").alias("value");

        System.out.println(c);

//        Main main = new Main();
//        main.configure().addRoutesBuilder(new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                errorHandler(deadLetterChannel("log:error").logExhausted(true)
//                        .useOriginalMessage().maximumRedeliveries(100).redeliveryDelay(1000));
//
//                from("timer:xxx?period=10000s")
//                        .log("${headers}").process(Test::process);
//            }
//        });
//        main.run();
    }

    private static void process(Exchange exchange) throws Exception {
        try{
            Dataset<Row> dataset = spark.sql("select * from casscatalog.infinity_ks.events_by_time;");
            Dataset<Row> avg = dataset.groupBy("event_group", "event_type").agg(functions.avg("value").alias("value"))
                    .withColumn("agg_type", functions.lit("AVG"))
                    .withColumn("horizon", functions.lit("MONTHS"))
                    .withColumn("agg_year", functions.lit(2020))
                    .withColumn("agg_month", functions.lit(8))
                    .withColumn("agg_day", functions.lit(14))
                    .withColumn("agg_hour", functions.lit(20))
                    .withColumn("agg_minute", functions.lit(20))
                    .withColumn("agg_second", functions.lit(20));
            avg.show();

            Map<String, String> tableProperties = new HashMap();
            tableProperties.put("keyspace", "infinity_ks");
            tableProperties.put("table", "aggregations");
            tableProperties.put("confirm.truncate", "true");
            avg.write().format("org.apache.spark.sql.cassandra").options(tableProperties).mode(SaveMode.Overwrite).save();

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (AssertionError a) {
            LOGGER.info(a.getMessage());
            throw new Exception(a.getMessage());
        }
    }
}
