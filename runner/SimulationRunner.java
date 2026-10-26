package cpusim.runner;

import cpusim.generator.IProcessGenerator;
import cpusim.metrics.AggregateMetrics;
import cpusim.metrics.IMetricsCalculator;
import cpusim.metrics.IMetricsCollector;
import cpusim.model.Process;
import cpusim.strategy.ISchedulingStrategy;

import java.util.List;
import java.util.Objects;

/**
 * Orchestrates "generate workload -> schedule -> measure" for N runs. Depends only on abstractions,
 * all of which are supplied through the constructor.
 */
public final class SimulationRunner implements ISimulationRunner {

    private final IProcessGenerator generator;
    private final ISchedulingStrategy strategy;
    private final IMetricsCalculator calculator;
    private final IMetricsCollector collector;
    private final ISimulationObserver observer;
    private final SimulationConfig config;

    public SimulationRunner(IProcessGenerator generator,
                            ISchedulingStrategy strategy,
                            IMetricsCalculator calculator,
                            IMetricsCollector collector,
                            ISimulationObserver observer,
                            SimulationConfig config) {
        this.generator = Objects.requireNonNull(generator, "generator");
        this.strategy = Objects.requireNonNull(strategy, "strategy");
        this.calculator = Objects.requireNonNull(calculator, "calculator");
        this.collector = Objects.requireNonNull(collector, "collector");
        this.observer = Objects.requireNonNull(observer, "observer");
        this.config = Objects.requireNonNull(config, "config");
    }

    @Override
    public AggregateMetrics run() {
        collector.reset();
        observer.onSimulationStarted(strategy.name(), config.totalRuns());

        for (int run = 1; run <= config.totalRuns(); run++) {
            List<Process> workload = generator.generate();
            List<Process> completed = strategy.runSimulation(workload);
            collector.collect(calculator.calculate(completed));

            if (config.isCheckpoint(run)) {
                observer.onCheckpoint(run, collector.snapshot());
            }
        }
        return collector.snapshot();
    }
}
