package one.entropy.infinity.aggregator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AggregatorCommand {

    private ChronoUnit horizon;
    private Instant timestamp;

    public AggregatorCommand() {
    }

    public AggregatorCommand(ChronoUnit horizon, Instant timestamp) {
        this.horizon = horizon;
        this.timestamp = timestamp;
    }

    public ChronoUnit getHorizon() {
        return horizon;
    }

    public void setHorizon(ChronoUnit horizon) {
        this.horizon = horizon;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AggregatorCommand{" +
                "horizon=" + horizon +
                ", timestamp=" + timestamp +
                '}';
    }
}
