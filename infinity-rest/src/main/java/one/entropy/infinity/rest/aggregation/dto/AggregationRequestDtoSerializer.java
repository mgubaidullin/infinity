package one.entropy.infinity.rest.aggregation.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import one.entropy.infinity.rest.event.dto.EventDto;

public class AggregationRequestDtoSerializer extends ObjectMapperSerializer<AggregationRequest> {
    public AggregationRequestDtoSerializer() {
        super();
    }
}