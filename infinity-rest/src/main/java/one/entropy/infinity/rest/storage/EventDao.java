package one.entropy.infinity.rest.storage;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.quarkus.runtime.api.reactive.mapper.MutinyMappedReactiveResultSet;

import java.time.Instant;

@Dao
public interface EventDao {
    @Select(customWhereClause = "event_group = :group and event_type = :type and event_timestamp < :timestamp")
    MutinyMappedReactiveResultSet<Event> findEvents(String group, String type, Instant timestamp);

}

