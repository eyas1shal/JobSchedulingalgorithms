package cpusim.metrics;

/** Averages over {@code runs} simulation runs. */
public record AggregateMetrics(long runs, double averageTurnaroundTime, double averageWaitingTime) { }
