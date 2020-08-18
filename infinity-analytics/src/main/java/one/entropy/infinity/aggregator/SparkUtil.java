package one.entropy.infinity.aggregator;

import com.datastax.spark.connector.CassandraSparkExtensions;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SparkUtil {
    private static final Logger LOGGER = Logger.getLogger(SparkUtil.class.getName());

    private static Map<String, String> aggOptions = Map.of(
            "keyspace", "infinity_ks",
            "table", "aggregations",
            "output.consistency.level", "ONE"
    );
    private static Map<String, String> predictionOptions = Map.of(
            "keyspace", "infinity_ks",
            "table", "predictions",
            "output.consistency.level", "ONE"
    );

    public static SparkSession createSparkSession(String host) {
        LOGGER.log(Level.INFO, "Creating SparkSession connected to: {0}", host);
        return SparkSession.builder()
                .appName("Infinity Aggregator")
                .master("local")
                .withExtensions(new CassandraSparkExtensions())
                .config("spark.cassandra.connection.host", host)
                .config("spark.cassandra.connection.port", "9042")
                .config("spark.sql.catalog.casscatalog", "com.datastax.spark.connector.datasource.CassandraCatalog")
                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.host", host)
                .config("spark.sql.catalog.casscatalog.spark.cassandra.connection.port", "9042")
                .getOrCreate();
    }

    public static void saveAggregations(Dataset<Row> dataset) {
        LOGGER.log(Level.INFO, "Save aggregations");
        dataset = dataset.drop("event_year").drop("event_month").drop("event_day").drop("event_hour").drop("event_minute").drop("event_second");
        dataset.write().format("org.apache.spark.sql.cassandra").options(aggOptions).mode(SaveMode.Append).save();
    }

    public static void savePredictions(Dataset<Row> dataset) {
        LOGGER.log(Level.INFO, "Save predictions");
        dataset.write().format("org.apache.spark.sql.cassandra").options(predictionOptions).mode(SaveMode.Append).save();
    }
}
