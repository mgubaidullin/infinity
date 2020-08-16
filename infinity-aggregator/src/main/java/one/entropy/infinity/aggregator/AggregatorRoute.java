package one.entropy.infinity.aggregator;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class AggregatorRoute extends EndpointRouteBuilder {

    private Aggregator aggregator;

    @Override
    public void configure() throws Exception {
        String host = getContext().resolvePropertyPlaceholders("{{cassandra.host}}");
        aggregator = new Aggregator(host);

        errorHandler(deadLetterChannel("log:error").logExhausted(true)
                .useOriginalMessage().maximumRedeliveries(100).redeliveryDelay(1000));

        from(kafka("agg-requests")).routeId("aggregator")
                .log("Aggregation request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, AggregationRequest.class)
                .process(aggregator::process)
                .log("Aggregation done for request: ${body}");
    }
}
