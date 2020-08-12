package one.entropy.infinity.processor.dto;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class EventDtoDeserializer extends ObjectMapperDeserializer<EventDto> {
    public EventDtoDeserializer() {
        super(EventDto.class);
    }
}