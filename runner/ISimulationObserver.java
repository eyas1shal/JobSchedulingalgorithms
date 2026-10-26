package cpusim.runner;

import cpusim.metrics.AggregateMetrics;

/** Receives progress notifications from a simulation. Keeps the runner free of any I/O. */
public interface ISimulationObserver {

    void onSimulationStarted(String algorithmName, int totalRuns);

    void onCheckpoint(int completedRuns, AggregateMetrics metrics);
}
