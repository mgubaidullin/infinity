package one.entropy.infinity.rest.event.storage;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
public class EventService {

  @Inject EventDao dao;

  public Multi<EventByTimestamp> getEventsByTimestamp(String group, String type, Instant timestamp) {
    return dao.findEventsByTimestamp(group, type, timestamp);
  }

  public Uni<EventById> getEventById(String group, String type, String id) {
    return dao.findEventById(group, type, id).toUni();
  }

}
