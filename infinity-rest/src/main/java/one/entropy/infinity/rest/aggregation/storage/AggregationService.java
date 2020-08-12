package one.entropy.infinity.rest.aggregation.storage;

import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
public class AggregationService {

    @Inject
    AggregationDao dao;

    public Multi<Aggregation> get(String eventGroup, String eventType, String type, String period, String time) {
        return dao.findAggregations(eventGroup, eventType, type, period, time);
    }

}
