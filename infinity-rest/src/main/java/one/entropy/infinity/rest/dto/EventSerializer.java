package one.entropy.infinity.rest.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class EventSerializer extends ObjectMapperSerializer<EventDto> {
    public EventSerializer() {
        super();
    }
}