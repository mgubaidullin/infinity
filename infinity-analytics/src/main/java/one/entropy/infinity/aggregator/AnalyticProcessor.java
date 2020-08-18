package one.entropy.infinity.aggregator;

import org.apache.camel.Exchange;
import org.apache.spark.sql.*;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalyticProcessor {
    private static final Logger LOGGER = Logger.getLogger(AnalyticProcessor.class.getName());

    private SparkSession spark;

    public AnalyticProcessor(String host) {
        spark = SparkUtil.createSparkSession(host);
    }

    protected void process(Exchange exchange) throws Exception {
        AnalyticRequest request = exchange.getIn().getBody(AnalyticRequest.class);
        LOGGER.log(Level.INFO, "Process request: {0}", request);
        try {
            Dataset<Row> dataset = selectEvents(request.getEventGroup(), request.getEventType());
            Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS)
                    .forEach(horizon -> {
                        Dataset<Row> aggregation = Aggregator.aggregate(dataset, horizon);
                        Dataset<Row> prediction = Predictor.forecast(aggregation, horizon, spark);

                        SparkUtil.saveAggregations(aggregation);
                        SparkUtil.savePredictions(prediction);
                    });
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        } catch (AssertionError a) {
            LOGGER.info(a.getMessage());
            throw new Exception(a.getMessage());
        }
    }

    private Dataset<Row> selectEvents(String eventGroup, String eventType) {
        LOGGER.log(Level.INFO, "Select events for eventGroup: {0} eventType: {1}", new String[]{eventGroup, eventType});
        Dataset<Row> dataset = spark.sql("select * from casscatalog.infinity_ks.events_by_time;")
                .where(new Column("event_group").equalTo(eventGroup))
                .where(new Column("event_type").equalTo(eventType));
        return dataset;
    }
}
