package one.entropy.infinity.rest.analytics.storage;

import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AnalyticDaoProducer {
    private final AnalyticDao analyticDao;

    @Inject
    public AnalyticDaoProducer(QuarkusCqlSession session) {
        AnalyticMapper mapper = new AnalyticMapperBuilder(session).build();
        analyticDao = mapper.analyticDao();
    }

    @Produces
    @ApplicationScoped
    AnalyticDao produceAnalyticDao() {
        return analyticDao;
    }
}
