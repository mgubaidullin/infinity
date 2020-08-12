package one.entropy.infinity.processor.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventService {

  @Inject EventDao dao;

  public void save(Event event) {
    dao.update(event);
  }

}
