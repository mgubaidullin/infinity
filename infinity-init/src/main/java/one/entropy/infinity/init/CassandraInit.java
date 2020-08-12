package one.entropy.infinity.init;

import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class CassandraInit {
    private static final Logger LOGGER = Logger.getLogger(CassandraInit.class.getName());

    private static final String ks =
            "CREATE KEYSPACE IF NOT EXISTS infinity_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};" ;

    private static final String events =
            "CREATE TABLE IF NOT EXISTS infinity_ks.events (event_group TEXT, event_type TEXT, event_timestamp TIMESTAMP, value DECIMAL, PRIMARY KEY ((event_group, event_type), event_timestamp)) WITH CLUSTERING ORDER BY (event_timestamp DESC);" ;

    private static final String aggregations =
            "CREATE TABLE IF NOT EXISTS infinity_ks.aggregations (event_group TEXT, event_type TEXT, agg_type TEXT, agg_period TEXT, agg_time TEXT, value DECIMAL, PRIMARY KEY ((event_group, event_type), agg_type, agg_period, agg_time)) WITH CLUSTERING ORDER BY (agg_type ASC, agg_period ASC, agg_time DESC)";

    private QuarkusCqlSession session;

    @Inject
    public CassandraInit(QuarkusCqlSession session) {
        this.session = session;
    }

    public void create() {
        LOGGER.info("Creating keyspace and tables...");
        session.execute(ks);
        session.execute(events);
        session.execute(aggregations);
        LOGGER.info("Created keyspace and tables");
    }

}
