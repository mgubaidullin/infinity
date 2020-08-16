package one.entropy.infinity.rest.event;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import one.entropy.infinity.rest.event.dto.EventDto;
import one.entropy.infinity.rest.event.storage.EventService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventResource.class.getName());

    @Inject
    @OnOverflow(OnOverflow.Strategy.BUFFER)
    @Channel("events")
    Emitter<EventDto> emitterForEvents;

    @POST
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Event published",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))})
    @Operation( summary = "Publish new event to the system")
    public Uni<EventDto> add(EventDto eventDto) {
        LOGGER.info("Received publish request for event: {}", eventDto.toString());
        eventDto.setId(eventDto.getId() !=null && !eventDto.getId().isEmpty() ? eventDto.getId() : UUID.randomUUID().toString());
        return Uni.createFrom().completionStage(emitterForEvents.send(eventDto)).onItem().apply(x -> eventDto);
    }

    @Inject
    EventService eventService;

    @GET
    @Path("/{group}/{type}/{timestamp}")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Events retrieved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))})
    @Operation( summary = "Get event list before timestamp")
    @Parameter(name = "group", description = "Group of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "type", description = "Type of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "timestamp", description = "Select events before the parameter value",example = "2222-08-12T15:52:42.942Z", required = true, schema = @Schema(type = SchemaType.STRING))
    public Multi<EventDto> getEventsByTimestamp(@PathParam("group") String group,  @PathParam("type") String type, @PathParam("timestamp") String timestamp) {
        Instant instant = Instant.parse(timestamp);
        return eventService
                .getEventsByTimestamp(group, type, instant)
                .map(e -> new EventDto(e.getId().toString(), e.getGroup(), e.getType(), e.getTimestamp().atZone(ZoneOffset.UTC), e.getValue()));
    }

    @GET
    @Path("/{group}/{type}/{id}")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Events retrieved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))})
    @Operation( summary = "Get event by ID")
    @Parameter(name = "group", description = "Group of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "type", description = "Type of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "id", description = "Event UUID", example = "45382a41-4d9f-4722-ab9b-0e7e524e4945", required = true, schema = @Schema(type = SchemaType.STRING))
    public Uni<EventDto> getEventById(@PathParam("group") String group,  @PathParam("type") String type, @PathParam("id") String id) {
        return eventService
                .getEventById(group, type, id)
                .map(e -> new EventDto(e.getId().toString(), e.getGroup(), e.getType(), e.getTimestamp().atZone(ZoneOffset.UTC), e.getValue()));
    }
}
