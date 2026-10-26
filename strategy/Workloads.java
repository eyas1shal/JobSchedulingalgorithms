package cpusim.strategy;

import cpusim.model.Process;

import java.util.List;
import java.util.Objects;

/** Package-private helper: gives each strategy its own pristine working copies. */
final class Workloads {

    private Workloads() { }

    static List<Process> freshCopies(List<Process> processes) {
        Objects.requireNonNull(processes, "processes");
        return processes.stream()
                .map(p -> Objects.requireNonNull(p, "process").copy())
                .toList();
    }
}
