package one.entropy.infinity.rest.event;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class EventSerializer extends ObjectMapperSerializer<Event> {
    public EventSerializer() {
        super();
    }
}