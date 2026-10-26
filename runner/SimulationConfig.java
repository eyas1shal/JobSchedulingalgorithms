package cpusim.runner;

import java.util.Set;

/**
 * @param totalRuns  how many random workloads to simulate
 * @param checkpoints run counts at which the observer is notified (the last run always is)
 */
public record SimulationConfig(int totalRuns, Set<Integer> checkpoints) {

    public SimulationConfig {
        if (totalRuns <= 0) {
            throw new IllegalArgumentException("totalRuns must be > 0");
        }
        checkpoints = Set.copyOf(checkpoints);
    }

    /** 100,000 runs reported at 100 / 1,000 / 10,000 / 100,000, like the original project. */
    public static SimulationConfig defaults() {
        return new SimulationConfig(100_000, Set.of(100, 1_000, 10_000));
    }

    boolean isCheckpoint(int run) {
        return run == totalRuns || checkpoints.contains(run);
    }
}
