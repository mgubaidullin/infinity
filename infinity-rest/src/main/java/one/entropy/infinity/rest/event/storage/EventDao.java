package one.entropy.infinity.rest.event.storage;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.quarkus.runtime.api.reactive.mapper.MutinyMappedReactiveResultSet;

import java.time.Instant;

@Dao
public interface EventDao {
    @Select(customWhereClause = "event_group = :group and event_type = :type and event_timestamp < :timestamp")
    MutinyMappedReactiveResultSet<EventByTimestamp> findEventsByTimestamp(String group, String type, Instant timestamp);

    @Select(customWhereClause = "event_group = :group and event_type = :type and id = :id")
    MutinyMappedReactiveResultSet<EventById> findEventById(String group, String type, String id);

}

