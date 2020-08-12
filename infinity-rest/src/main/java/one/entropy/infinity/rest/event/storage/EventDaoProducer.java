package one.entropy.infinity.rest.event.storage;

import com.datastax.oss.quarkus.runtime.api.session.QuarkusCqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class EventDaoProducer {
    private final EventDao eventDao;

    @Inject
    public EventDaoProducer(QuarkusCqlSession session) {
        EventMapper mapper = new EventMapperBuilder(session).build();
        eventDao = mapper.eventDao();
    }

    @Produces
    @ApplicationScoped
    EventDao produceEventDao() {
        return eventDao;
    }
}
