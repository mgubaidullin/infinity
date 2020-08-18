package one.entropy.infinity.rest.analytics.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class AnalyticRequestSerializer extends ObjectMapperSerializer<AnalyticRequest> {
    public AnalyticRequestSerializer() {
        super();
    }
}