package cpusim.runner;

import cpusim.generator.IProcessGenerator;
import cpusim.metrics.IMetricsCalculator;
import cpusim.metrics.IMetricsCollector;
import cpusim.strategy.ISchedulingStrategy;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultSimulationRunnerFactory implements ISimulationRunnerFactory {

    private final IProcessGenerator generator;
    private final IMetricsCalculator calculator;
    private final Supplier<? extends IMetricsCollector> collectorSupplier;
    private final SimulationConfig config;

    public DefaultSimulationRunnerFactory(IProcessGenerator generator,
                                          IMetricsCalculator calculator,
                                          Supplier<? extends IMetricsCollector> collectorSupplier,
                                          SimulationConfig config) {
        this.generator = Objects.requireNonNull(generator, "generator");
        this.calculator = Objects.requireNonNull(calculator, "calculator");
        this.collectorSupplier = Objects.requireNonNull(collectorSupplier, "collectorSupplier");
        this.config = Objects.requireNonNull(config, "config");
    }

    @Override
    public ISimulationRunner create(ISchedulingStrategy strategy, ISimulationObserver observer) {
        return new SimulationRunner(generator, strategy, calculator, collectorSupplier.get(), observer, config);
    }
}
