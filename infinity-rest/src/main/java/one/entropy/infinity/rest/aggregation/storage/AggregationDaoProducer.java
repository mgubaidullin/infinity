package one.entropy.infinity.rest.aggregation.storage;

import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AggregationDaoProducer {
    private final AggregationDao aggregationDao;

    @Inject
    public AggregationDaoProducer(QuarkusCqlSession session) {
        AggregationMapper mapper = new AggregationMapperBuilder(session).build();
        aggregationDao = mapper.aggregationDao();
    }

    @Produces
    @ApplicationScoped
    AggregationDao produceAggregationDao() {
        return aggregationDao;
    }
}
