package one.entropy.infinity.rest.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Event {
    private String key;
    private LocalDateTime timestamp;
    private String type;
    private BigDecimal value;

    public Event() {
    }

    public Event(String key, LocalDateTime timestamp, String type, BigDecimal value) {
        this.key = key;
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
