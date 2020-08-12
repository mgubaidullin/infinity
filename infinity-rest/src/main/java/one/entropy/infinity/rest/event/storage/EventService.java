package one.entropy.infinity.rest.event.storage;

import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
public class EventService {

  @Inject EventDao dao;

  public Multi<Event> get(String group, String type, Instant timestamp) {
    return dao.findEvents(group, type, timestamp);
  }

}
