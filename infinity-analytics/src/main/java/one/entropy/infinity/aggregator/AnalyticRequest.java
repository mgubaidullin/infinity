package one.entropy.infinity.aggregator;

public class AnalyticRequest {

    private String eventGroup;
    private String eventType;

    public AnalyticRequest() {
    }

    public AnalyticRequest(String eventGroup, String eventType) {
        this.eventGroup = eventGroup;
        this.eventType = eventType;
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

    @Override
    public String toString() {
        return "AnalyticRequest{" +
                "eventGroup='" + eventGroup + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}

