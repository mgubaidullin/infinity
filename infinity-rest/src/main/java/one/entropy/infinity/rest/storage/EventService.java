package one.entropy.infinity.rest.storage;

import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class EventService {

  @Inject EventDao dao;

  public Multi<Event> get(String group, String type, Instant timestamp) {
    return dao.findEvents(group, type, timestamp);
  }

}
