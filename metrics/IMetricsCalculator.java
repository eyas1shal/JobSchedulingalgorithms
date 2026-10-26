package cpusim.metrics;

import cpusim.model.Process;

import java.util.List;

/** Computes metrics for one completed simulation. */
public interface IMetricsCalculator {

    /**
     * @param completed processes that all have finished
     * @throws IllegalArgumentException if the list is empty or contains an unfinished process
     */
    RunMetrics calculate(List<Process> completed);
}
