package cpusim.runner;

import cpusim.strategy.ISchedulingStrategy;
import cpusim.strategy.ISchedulingStrategyFactory;
import cpusim.strategy.IStrategyCatalog;

import java.util.Objects;

/**
 * Application loop: ask which algorithm to run, run it, repeat until the user quits.
 *
 * <p>It never names a concrete algorithm. New strategies appear automatically because the menu is
 * built from {@link IStrategyCatalog}.
 */
public final class SimulationApplication {

    private final IAlgorithmSelector selector;
    private final IStrategyCatalog catalog;
    private final ISchedulingStrategyFactory strategyFactory;
    private final ISimulationRunnerFactory runnerFactory;
    private final ISimulationObserver observer;

    public SimulationApplication(IAlgorithmSelector selector,
                                 IStrategyCatalog catalog,
                                 ISchedulingStrategyFactory strategyFactory,
                                 ISimulationRunnerFactory runnerFactory,
                                 ISimulationObserver observer) {
        this.selector = Objects.requireNonNull(selector, "selector");
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.strategyFactory = Objects.requireNonNull(strategyFactory, "strategyFactory");
        this.runnerFactory = Objects.requireNonNull(runnerFactory, "runnerFactory");
        this.observer = Objects.requireNonNull(observer, "observer");
    }

    public void run() {
        while (true) {
            var choice = selector.select(catalog.descriptors());
            if (choice.isEmpty()) {
                return;
            }
            ISchedulingStrategy strategy = strategyFactory.create(choice.get())
                    .orElseThrow(() -> new IllegalStateException("Selector returned unknown key: " + choice.get()));
            runnerFactory.create(strategy, observer).run();
        }
    }
}
