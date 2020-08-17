package one.entropy.infinity.rest.aggregation;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import one.entropy.infinity.rest.aggregation.dto.AggregationDto;
import one.entropy.infinity.rest.aggregation.dto.AggregationRequest;
import one.entropy.infinity.rest.aggregation.storage.AggregationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Path("/aggregation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AggregationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationResource.class.getName());

    @Inject
    @Channel("agg-requests")
    Emitter<AggregationRequest> emitterForRequests;

    @POST
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Request published",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))})
    @Operation( summary = "Publish aggregation request")
    public Uni<AggregationRequest> request(AggregationRequest request) {
        LOGGER.info("Received aggregation request: {}", request.toString());
        return Uni.createFrom().completionStage(emitterForRequests.send(request)).onItem().apply(x -> request);
    }

    @Inject
    AggregationService aggregationService;

    @GET
    @Path("/{eventGroup}/{eventType}/{horizon}/{period}")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Aggregation retrieved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))})
    @Operation( summary = "Get aggregations list")
    @Parameter(name = "eventGroup", description = "Group of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "eventType", description = "Type of events", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "horizon", description = "Horizon of aggregation (SECONDS, MINUTES, DAYS, MONTHS, YEARS)", example = "YEARS", required = true, schema = @Schema(type = SchemaType.STRING))
    @Parameter(name = "period", description = "Period of aggregation", example = "2020", required = true, schema = @Schema(type = SchemaType.STRING))
    public Multi<AggregationDto> getAggregations(
            @PathParam("eventGroup") String eventGroup,
            @PathParam("eventType") String eventType,
            @PathParam("horizon") String horizon,
            @PathParam("period") String period) {
        return aggregationService
                .get(eventGroup, eventType, horizon, period)
                .map(a -> new AggregationDto(a.getEventGroup(), a.getEventType(), a.getHorizon(), a.getPeriod(),
                        a.getAvgValue(), a.getMinValue(), a.getMaxValue(), a.getSumValue(), a.getMeanValue(), a.getCountValue()
                ));
    }
}
