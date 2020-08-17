package one.entropy.infinity.rest.aggregation.storage;

import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AggregationService {

    @Inject
    AggregationDao dao;

    public Multi<Aggregation> get(String eventGroup, String eventType, String horizon, String period) {
        return dao.findAggregations(eventGroup, eventType, horizon, period);
    }

}
