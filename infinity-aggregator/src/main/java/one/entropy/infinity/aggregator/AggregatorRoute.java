package one.entropy.infinity.aggregator;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

public class AggregatorRoute extends EndpointRouteBuilder {

    private Aggregator aggregator;

    @Override
    public void configure() throws Exception {
        String host = getContext().resolvePropertyPlaceholders("{{cassandra.host}}");
        aggregator = new Aggregator(host);

        errorHandler(deadLetterChannel("log:error").logExhausted(true)
                .useOriginalMessage().maximumRedeliveries(100).redeliveryDelay(1000));

        from(seda("aggregator")).routeId("aggregator")
                .process(aggregator::process)
                .log("Done");
    }
}
