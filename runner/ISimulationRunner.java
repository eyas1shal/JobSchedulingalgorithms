package cpusim.runner;

import cpusim.metrics.AggregateMetrics;

/** Executes one configured simulation to completion. */
public interface ISimulationRunner {

    AggregateMetrics run();
}
