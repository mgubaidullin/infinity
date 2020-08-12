package one.entropy.infinity.processor.storage;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@CqlName("events")
public class Event {

    @PartitionKey
    private UUID key;
    @ClusteringColumn
    @CqlName("event_timestamp")
    private Instant eventTimestamp;
    private String type;
    private BigDecimal value;

    public Event() {
    }

    public Event(UUID key, java.time.Instant eventTimestamp, String type, BigDecimal value) {
        this.key = key;
        this.eventTimestamp = eventTimestamp;
        this.type = type;
        this.value = value;
    }

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
