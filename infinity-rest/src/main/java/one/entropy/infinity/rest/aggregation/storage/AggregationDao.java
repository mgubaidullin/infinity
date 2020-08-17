package one.entropy.infinity.rest.aggregation.storage;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.quarkus.runtime.api.reactive.mapper.MutinyMappedReactiveResultSet;

@Dao
public interface AggregationDao {
    @Select(customWhereClause =
            "event_group = :eventGroup and event_type = :eventType and horizon = :horizon and period < :period ")
    MutinyMappedReactiveResultSet<Aggregation> findAggregations(
            String eventGroup, String eventType, String horizon, String period);

}



