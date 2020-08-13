package one.entropy.infinity.processor;

import com.datastax.driver.core.Cluster;
import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

public class ProcessorRoute extends EndpointRouteBuilder {

    private final String CQL = "insert into events(event_group, event_type, event_timestamp, value) values (?, ?, ?, ?)";

    @ConfigProperty(name = "cassandra", defaultValue = "localhost")
    String cassandra;

    @Named("clusterRef")
    Cluster cluster() {
        return Cluster.builder().addContactPoint(cassandra).build();
    }

    public void configure() throws Exception {

        errorHandler(deadLetterChannel(kafka("dead-letter-queue").getUri()).logExhausted(true)
                .useOriginalMessage().maximumRedeliveries(5).redeliveryDelay(1000));

        from(kafka("events").autoOffsetReset("earliest"))
                .log("Event received ${body}")
                .unmarshal().json(JsonLibrary.Jackson, EventDto.class)
                .process(this::process)
                .to("cql:bean:clusterRef/infinity_ks?cql=" + CQL)
                .log("Event stored to Cassandra");
    }

    private void process(Exchange exchange) {
            EventDto e = exchange.getIn().getBody(EventDto.class);
            List params = List.of(
                    e.getGroup(),
                    e.getType(),
                    new Timestamp(e.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                    e.getValue());
            exchange.getIn().setBody(params);
        }
    }
