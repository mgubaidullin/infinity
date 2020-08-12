package one.entropy.infinity.processor;

import one.entropy.infinity.processor.dto.EventDto;
import one.entropy.infinity.processor.storage.Event;
import one.entropy.infinity.processor.storage.EventService;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZoneId;

@ApplicationScoped
public class Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

    @Inject
    EventService eventService;

    @Incoming("events")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    void save(EventDto e) {
        LOGGER.info("Event received: {}", e.toString());
        Event event = new Event(e.getGroup(), e.getType(), e.getTimestamp().atZone(ZoneId.systemDefault()).toInstant(),e.getValue());
        eventService.save(event);
        LOGGER.info("Event saved: {}", e.toString());
    }
}
