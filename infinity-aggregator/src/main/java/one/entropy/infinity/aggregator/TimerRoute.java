package one.entropy.infinity.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

//        Every 10 second
        from(quartz("second").cron("0/10 0 0 ? * * *")).routeId("second")
                .log("${headers}")
                .process(e -> process(e, ChronoUnit.SECONDS))
                .to(seda("aggregator"));

//        Every minute
        from(quartz("minute").cron("0 * * ? * *")).routeId("minute")
                .process(e -> process(e, ChronoUnit.MINUTES))
                .to(seda("aggregator"));

//        Every hour
        from(quartz("hour").cron("0 0 * ? * *")).routeId("hour")
                .process(e -> process(e, ChronoUnit.HOURS))
                .to(seda("aggregator"));

//        Every day at 1am
        from(quartz("day").cron("0 0 1 * * ?")).routeId("day")
                .process(e -> process(e, ChronoUnit.DAYS))
                .to(seda("aggregator"));

//        Every month on the 1st, at noon
        from(quartz("month").cron("0 0 12 1 * ?")).routeId("month")
                .process(e -> process(e, ChronoUnit.MONTHS))
                .to(seda("aggregator"));
    }

    private void process(Exchange exchange, ChronoUnit horizon) {
        Date date = exchange.getIn().getHeader("fireTime", Date.class);
        AggregatorCommand command = new AggregatorCommand();
        command.setHorizon(horizon);
        command.setTimestamp(date.toInstant());
        exchange.getIn().setBody(command);
    }


}
