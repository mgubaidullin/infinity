package one.entropy.infinity.rest.event.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class EventDtoSerializer extends ObjectMapperSerializer<EventDto> {
    public EventDtoSerializer() {
        super();
    }
}