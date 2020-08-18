package one.entropy.infinity.aggregator;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class AnalyticRoute extends EndpointRouteBuilder {

    private AnalyticProcessor analyticProcessor;

    @Override
    public void configure() throws Exception {
        String host = getContext().resolvePropertyPlaceholders("{{cassandra.host}}");
        analyticProcessor = new AnalyticProcessor(host);

        errorHandler(deadLetterChannel("log:error").logExhausted(true)
                .useOriginalMessage().maximumRedeliveries(100).redeliveryDelay(1000));

        from(kafka("analytics-requests")).routeId("analytic")
                .log("Analytic request: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, AnalyticRequest.class)
                .process(analyticProcessor::process)
                .log("Analytic done for request: ${body}");
    }
}
