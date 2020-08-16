package one.entropy.infinity.rest.aggregation.dto;

public class AggregationRequest {

    private String eventGroup;
    private String eventType;
    private String horizon;

    public AggregationRequest() {
    }

    public AggregationRequest(String eventGroup, String eventType, String horizon) {
        this.eventGroup = eventGroup;
        this.eventType = eventType;
        this.horizon = horizon;
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

    public String getHorizon() {
        return horizon;
    }

    public void setHorizon(String horizon) {
        this.horizon = horizon;
    }

    @Override
    public String toString() {
        return "AggregationRequest{" +
                "eventGroup='" + eventGroup + '\'' +
                ", eventType='" + eventType + '\'' +
                ", horizon='" + horizon + '\'' +
                '}';
    }
}

