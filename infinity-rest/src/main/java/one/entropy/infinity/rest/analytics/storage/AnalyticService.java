package one.entropy.infinity.rest.analytics.storage;

import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AnalyticService {

    @Inject
    AnalyticDao dao;

    public Multi<Aggregation> getAggregations(String eventGroup, String eventType, String horizon, String period) {
        return dao.findAggregations(eventGroup, eventType, horizon, period);
    }

    public Multi<Prediction> getPredictions(String eventGroup, String eventType, String algorithm, String horizon, String period) {
        return dao.findPredictions(eventGroup, eventType, algorithm, horizon, period);
    }

}
