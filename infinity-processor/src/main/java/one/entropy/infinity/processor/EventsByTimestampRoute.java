package one.entropy.infinity.processor;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public class EventsByTimestampRoute extends EndpointRouteBuilder {

    private final String GROUP_ID = "events-by-timestamp";
    private final String CQL = "insert into events_by_timestamp (id, event_group, event_type, event_timestamp, value) values (?, ?, ?, ?, ?)";

    public void configure() throws Exception {

        errorHandler(deadLetterChannel(kafka("dead-letter-queue").getUri()).logExhausted(true)
                .useOriginalMessage().maximumRedeliveries(3).redeliveryDelay(1000));

        from(kafka("events").groupId(GROUP_ID).autoOffsetReset("earliest"))
                .routeId(GROUP_ID)
                .log("Event received ${body}")
                .unmarshal().json(JsonLibrary.Jackson, EventDto.class)
                .process(this::process)
                .to("cql:bean:clusterRef/infinity_ks?cql=" + CQL)
                .log("Event stored to Cassandra");
    }

    private void process(Exchange exchange) {
        EventDto e = exchange.getIn().getBody(EventDto.class);
        List params = List.of(
                UUID.fromString(e.getId()),
                e.getGroup(),
                e.getType(),
                new Timestamp(e.getTimestamp().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()),
                e.getValue());
        exchange.getIn().setBody(params);
    }
}