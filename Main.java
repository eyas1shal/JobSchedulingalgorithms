package cpusim;

import cpusim.generator.IProcessGenerator;
import cpusim.generator.ProcessGeneratorConfig;
import cpusim.generator.RandomProcessGenerator;
import cpusim.metrics.AveragingMetricsCollector;
import cpusim.metrics.MetricsCalculator;
import cpusim.runner.DefaultSimulationRunnerFactory;
import cpusim.runner.SimulationApplication;
import cpusim.runner.SimulationConfig;
import cpusim.strategy.FCFSStrategy;
import cpusim.strategy.MLFQStrategy;
import cpusim.strategy.RRStrategy;
import cpusim.strategy.SRTFStrategy;
import cpusim.strategy.SchedulingStrategyRegistry;
import cpusim.ui.ConsoleUI;

import java.util.Random;

/**
 * Composition root: the only place that knows concrete types and wires them together.
 * Optional argument: a {@code long} seed for reproducible runs.
 */
public final class Main {

    private Main() { }

    public static void main(String[] args) {
        Random random = args.length > 0 ? new Random(Long.parseLong(args[0])) : new Random();

        // To add an algorithm, add one register(...) line here. Nothing else changes.
        SchedulingStrategyRegistry registry = new SchedulingStrategyRegistry()
                .register("srtf", SRTFStrategy::new)
                .register("fcfs", FCFSStrategy::new)
                .register("rr", () -> new RRStrategy(20))
                .register("mlfq", MLFQStrategy::new);

        IProcessGenerator generator = new RandomProcessGenerator(ProcessGeneratorConfig.defaults(), random);
        ConsoleUI ui = new ConsoleUI(System.in, System.out);

        new SimulationApplication(
                ui,
                registry,
                registry,
                new DefaultSimulationRunnerFactory(
                        generator, new MetricsCalculator(), AveragingMetricsCollector::new, SimulationConfig.defaults()),
                ui
        ).run();
    }
}
