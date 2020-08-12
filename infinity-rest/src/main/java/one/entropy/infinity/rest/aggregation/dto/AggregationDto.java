package one.entropy.infinity.rest.aggregation.dto;

import java.math.BigDecimal;

public class AggregationDto {

    private String eventGroup;
    private String eventType;
    private String type;
    private String period;
    private String time;
    private BigDecimal value;

    public AggregationDto() {
    }

    public AggregationDto(String eventGroup, String eventType, String type, String period, String time, BigDecimal value) {
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

