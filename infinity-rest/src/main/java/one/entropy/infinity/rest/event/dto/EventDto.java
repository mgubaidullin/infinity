package one.entropy.infinity.rest.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class EventDto {

    private String id;
    private String group;
    private String type;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime timestamp;
    private BigDecimal value;

    public EventDto() {
    }

    public EventDto(String id, String group, String type, ZonedDateTime timestamp, BigDecimal value) {
        this.id = id;
        this.group = group;
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
