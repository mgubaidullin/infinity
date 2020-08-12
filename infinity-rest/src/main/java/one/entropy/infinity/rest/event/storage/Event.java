package one.entropy.infinity.rest.event.storage;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@CqlName("events")
public class Event {

    @PartitionKey(1)
    @CqlName("event_group")
    private String group;
    @PartitionKey(2)
    @CqlName("event_type")
    private String type;
    @ClusteringColumn
    @CqlName("event_timestamp")
    private Instant timestamp;
    private BigDecimal value;

    public Event() {
    }

    public Event(String group, String type, Instant timestamp, BigDecimal value) {
        this.group = group;
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
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
}
