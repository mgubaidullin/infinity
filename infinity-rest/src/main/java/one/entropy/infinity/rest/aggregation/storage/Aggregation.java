package one.entropy.infinity.rest.aggregation.storage;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.math.BigDecimal;

@Entity
@CqlName("aggregations")
public class Aggregation {

    @PartitionKey(1)
    @CqlName("event_group")
    private String eventGroup;
    @PartitionKey(2)
    @CqlName("event_type")
    private String eventType;
    @ClusteringColumn(1)
    @CqlName("agg_type")
    private String type;
    @ClusteringColumn(2)
    @CqlName("agg_period")
    private String period;
    @ClusteringColumn(3)
    @CqlName("agg_time")
    private String time;
    private BigDecimal value;

    public Aggregation() {
    }

    public Aggregation(String eventGroup, String eventType, String type, String period, String time, BigDecimal value) {
        this.eventGroup = eventGroup;
        this.eventType = eventType;
        this.type = type;
        this.period = period;
        this.time = time;
        this.value = value;
    }

    public String getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(String eventGroup) {
        this.eventGroup = eventGroup;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
