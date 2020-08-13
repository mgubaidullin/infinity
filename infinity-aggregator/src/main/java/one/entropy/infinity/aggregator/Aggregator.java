package one.entropy.infinity.aggregator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aggregator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Aggregator.class.getName());

    public static void main(String[] args) {
        // Running Spark version 2.4.0.14
        Aggregator a = new Aggregator();
        a.onStart();
    }

    void onStart() {
        LOGGER.info("The application is starting...");
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .config("spark.master", "local")
                .config("spark.sql.catalog.cass100", "com.datastax.spark.connector.datasource.CassandraCatalog")
                .config("spark.sql.catalog.cass100.spark.cassandra.connection.host", "localhost")
//                .withExtensions(new CassandraSparkExtensions())
                .getOrCreate();
//        SparkConf conf = new SparkConf()
//                .setAppName( "My application")
//                .set("spark.cassandra.connection.host","localhost")
//                .setMaster("local");
//        System.out.println(conf);
//        SparkContext sc = new SparkContext(conf);
//
//        System.out.println(sc.logName());
//
//        spark.conf().set("spark.sql.catalog.cass100", "com.datastax.spark.connector.datasource.CassandraCatalog");
//        spark.conf().set("spark.sql.catalog.cass100.spark.cassandra.connection.host", "localhost");
        Dataset<Row> df = spark.sql("select * from cass100.infinity_ks.events;");

        df.show();


//        JavaRDD<String> cassandraRowsRDD = javaFunctions(sc).cassandraTable("infinity_ks", "events")
//                .map(new Function<CassandraRow, String>() {
//                    @Override
//                    public String call(CassandraRow cassandraRow) throws Exception {
//                        return cassandraRow.toString();
//                    }
//                });
//        System.out.println("Data as CassandraRows: \n" + StringUtils.join(cassandraRowsRDD.collect(), "\n"));


//        SparkConf conf = new SparkConf().setAppName("Infinity Aggregator");
//        SparkContext sc = new SparkContext(conf);
//
//        JavaRDD cassandraRdd = CassandraJavaUtil.javaFunctions(sc)
//                .cassandraTable("my_keyspace", "my_table").mapColumnTo(String.class)).select("my_column");
    }
}
