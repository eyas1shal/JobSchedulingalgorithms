package cpusim.runner;

import cpusim.strategy.ISchedulingStrategy;

/** Builds a runner for a chosen strategy; hides how the remaining collaborators are wired. */
public interface ISimulationRunnerFactory {

    ISimulationRunner create(ISchedulingStrategy strategy, ISimulationObserver observer);
}
