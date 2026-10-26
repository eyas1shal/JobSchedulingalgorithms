package cpusim.metrics;

/** Averages for one simulation run. */
public record RunMetrics(double averageTurnaroundTime, double averageWaitingTime) { }
