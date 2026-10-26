package cpusim.metrics;

/** Accumulates per-run metrics across many simulation runs. */
public interface IMetricsCollector {

    void collect(RunMetrics run);

    AggregateMetrics snapshot();

    void reset();
}
