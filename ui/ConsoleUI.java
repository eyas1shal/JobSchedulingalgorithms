package cpusim.ui;

import cpusim.metrics.AggregateMetrics;
import cpusim.runner.IAlgorithmSelector;
import cpusim.runner.ISimulationObserver;
import cpusim.strategy.StrategyDescriptor;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console adapter. It is the only class that touches stdin/stdout, and it implements the two small
 * ports the application needs ({@link IAlgorithmSelector} and {@link ISimulationObserver}).
 * It does not own the supplied streams and therefore never closes them.
 */
public final class ConsoleUI implements IAlgorithmSelector, ISimulationObserver {

    private final Scanner in;
    private final PrintStream out;

    public ConsoleUI(InputStream input, PrintStream out) {
        this.in = new Scanner(Objects.requireNonNull(input, "input"));
        this.out = Objects.requireNonNull(out, "out");
    }

    @Override
    public Optional<String> select(List<StrategyDescriptor> options) {
        out.println();
        out.println("Choose an algorithm to schedule random processes:");
        for (int i = 0; i < options.size(); i++) {
            out.printf("%d - %s%n", i + 1, options.get(i).displayName());
        }
        out.println("Anything else closes the app.");
        out.print("> ");
        out.flush();

        if (!in.hasNext()) {
            return Optional.empty();
        }
        try {
            int index = Integer.parseInt(in.next().trim());
            if (index >= 1 && index <= options.size()) {
                return Optional.of(options.get(index - 1).key());
            }
        } catch (NumberFormatException ignored) {
            // falls through: any non-menu input means "quit"
        }
        return Optional.empty();
    }

    @Override
    public void onSimulationStarted(String algorithmName, int totalRuns) {
        out.println();
        out.printf("%s - %,d random simulations%n", algorithmName, totalRuns);
        out.printf("%10s %14s %14s%n", "runs", "ATT", "AWT");
    }

    @Override
    public void onCheckpoint(int completedRuns, AggregateMetrics metrics) {
        out.printf("%,10d %14.4f %14.4f%n",
                completedRuns, metrics.averageTurnaroundTime(), metrics.averageWaitingTime());
    }
}
