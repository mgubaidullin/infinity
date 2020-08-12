package one.entropy.infinity.rest.event;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import one.entropy.infinity.rest.event.Event;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    @Channel("events")
    Emitter<Event> emitterForEvents;

    @POST
    public Uni<Event> add(Event event){
        return Uni.createFrom().completionStage(emitterForEvents.send(event)).onItem().apply(x -> event);
    }

    @GET
    @Path("/{length}")
    public Multi<Event> getEvents(@PathParam("length") int length) {
        return Multi.createFrom().range(0, 10)
                .map(i -> new Event(i.toString(), LocalDateTime.now(), "type", new BigDecimal(i)))
                .collectItems().asList()
                .onItem().produceMulti(s -> Multi.createFrom().iterable(s));
    }
}
