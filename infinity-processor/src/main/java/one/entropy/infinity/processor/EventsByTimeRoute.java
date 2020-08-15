package one.entropy.infinity.processor;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.UUID;

public class EventsByTimeRoute extends EndpointRouteBuilder {

    private final String GROUP_ID = "events-by-time";
    private final String CQL = "insert into events_by_time " +
            "(event_timestamp, event_group, event_type, event_year, event_month, event_day, event_hour, event_minute, event_second, value) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        ZonedDateTime timestamp = e.getTimestamp().atZone(ZoneOffset.UTC);
        List params = List.of(
                new Timestamp(e.getTimestamp().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()),
                e.getGroup(),
                e.getType(),
                timestamp.get(ChronoField.YEAR),
                timestamp.get(ChronoField.MONTH_OF_YEAR),
                timestamp.get(ChronoField.DAY_OF_MONTH),
                timestamp.get(ChronoField.HOUR_OF_DAY),
                timestamp.get(ChronoField.MINUTE_OF_HOUR),
                timestamp.get(ChronoField.SECOND_OF_MINUTE),
                e.getValue());
        exchange.getIn().setBody(params);
    }
}
