package one.entropy.infinity.rest.event.storage;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@CqlName("events_by_id")
public class EventById {

    @PartitionKey(1)
    @CqlName("event_group")
    private String group;
    @PartitionKey(2)
    @CqlName("event_type")
    private String type;
    @ClusteringColumn
    private UUID id;
    private BigDecimal value;
    @CqlName("event_timestamp")
    private Instant timestamp;

    public EventById() {
    }

    public EventById(String group, String type, Instant timestamp, BigDecimal value, UUID id) {
        this.group = group;
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
