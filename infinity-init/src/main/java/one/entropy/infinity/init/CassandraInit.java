package one.entropy.infinity.init;

import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class CassandraInit {
    private static final Logger LOGGER = Logger.getLogger(CassandraInit.class.getName());

    private static final String KS =
            "CREATE KEYSPACE IF NOT EXISTS infinity_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};";

    private static final String EVENTS_BY_ID =
            "CREATE TABLE IF NOT EXISTS infinity_ks.events_by_id " +
                    "(id UUID, event_group TEXT, event_type TEXT, event_timestamp TIMESTAMP, value DECIMAL, " +
                    "PRIMARY KEY ((event_group, event_type), id));";

    private static final String EVENTS_BY_TIMESTAMP =
            "CREATE TABLE IF NOT EXISTS infinity_ks.events_by_timestamp " +
                    "(id UUID, event_group TEXT, event_type TEXT, event_timestamp TIMESTAMP, value DECIMAL, " +
                    "PRIMARY KEY ((event_group, event_type), event_timestamp))  " +
                    "WITH CLUSTERING ORDER BY (event_timestamp DESC);";


    private static final String EVENTS_BY_TIME =
            "CREATE TABLE IF NOT EXISTS infinity_ks.events_by_time " +
                    " (event_timestamp TIMESTAMP, event_group TEXT, event_type TEXT, event_year INT, event_month INT, event_day INT, event_hour INT, event_minute INT, event_second INT, value DECIMAL, " +
                    " PRIMARY KEY ((event_group, event_type), event_timestamp, event_year, event_month, event_day, event_hour, event_minute, event_second)) " +
                    " WITH CLUSTERING ORDER BY (" +
                    " event_timestamp DESC, event_year DESC, event_month DESC, event_day DESC, event_hour DESC, event_minute DESC, event_second DESC" +
                    ");";

    private static final String AGGREGATIONS =
            "CREATE TABLE IF NOT EXISTS infinity_ks.aggregations " +
                    "(event_group TEXT, event_type TEXT, horizon TEXT, period TEXT, " +
                    "avg_value DECIMAL, min_value DECIMAL, max_value DECIMAL, sum_value DECIMAL, mean_value DECIMAL, count_value DECIMAL," +
                    "PRIMARY KEY ((event_group, event_type), horizon, period)) " +
                    "WITH CLUSTERING ORDER BY (" +
                    "horizon ASC, period DESC" +
                    ")";

    private static final String PREDICTIONS =
            "CREATE TABLE IF NOT EXISTS infinity_ks.predictions " +
                    "(event_group TEXT, event_type TEXT, algorithm TEXT, horizon TEXT, period TEXT, " +
                    " avg_value DECIMAL, min_value DECIMAL, max_value DECIMAL, sum_value DECIMAL, mean_value DECIMAL, count_value DECIMAL, " +
                    "PRIMARY KEY ((event_group, event_type), algorithm, horizon, period)) " +
                    "WITH CLUSTERING ORDER BY (" +
                    "algorithm ASC, horizon ASC, period DESC" +
                    ")";

    private QuarkusCqlSession session;

    @Inject
    public CassandraInit(QuarkusCqlSession session) {
        this.session = session;
    }

    public void create() {
        LOGGER.info("Creating keyspace and tables...");
        session.execute(KS);
        session.execute(EVENTS_BY_ID);
        session.execute(EVENTS_BY_TIMESTAMP);
        session.execute(EVENTS_BY_TIME);
        session.execute(AGGREGATIONS);
        session.execute(PREDICTIONS);
        LOGGER.info("Created keyspace and tables");
    }

}
