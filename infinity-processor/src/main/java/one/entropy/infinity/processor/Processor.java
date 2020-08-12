package one.entropy.infinity.processor;

import one.entropy.infinity.processor.storage.Event;
import one.entropy.infinity.processor.storage.EventService;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZoneId;
import java.util.UUID;

@ApplicationScoped
public class Processor {

    @Inject
    EventService eventService;

    @Incoming("events")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    void save(EventDto e) {
        Event event = new Event(UUID.fromString(e.getKey()), e.getTimestamp().atZone(ZoneId.systemDefault()).toInstant(), e.getType(),e.getValue());
        eventService.save(event);
    }
}
