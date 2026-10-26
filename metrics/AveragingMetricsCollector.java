package cpusim.metrics;

import java.util.Objects;

/** Keeps running sums so memory use is O(1) regardless of the number of runs. Not thread-safe. */
public final class AveragingMetricsCollector implements IMetricsCollector {

    private long runs;
    private double turnaroundSum;
    private double waitingSum;

    @Override
    public void collect(RunMetrics run) {
        Objects.requireNonNull(run, "run");
        runs++;
        turnaroundSum += run.averageTurnaroundTime();
        waitingSum += run.averageWaitingTime();
    }

    @Override
    public AggregateMetrics snapshot() {
        if (runs == 0) {
            return new AggregateMetrics(0, 0.0, 0.0);
        }
        return new AggregateMetrics(runs, turnaroundSum / runs, waitingSum / runs);
    }

    @Override
    public void reset() {
        runs = 0;
        turnaroundSum = 0;
        waitingSum = 0;
    }
}
