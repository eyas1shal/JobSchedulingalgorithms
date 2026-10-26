package cpusim.strategy;

import cpusim.model.Process;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Package-private helper that releases processes in arrival order (ties broken by pid) as the
 * simulation clock advances. One instance per simulation run.
 */
final class ArrivalQueue {

    private final Deque<Process> pending;

    ArrivalQueue(Collection<Process> processes) {
        this.pending = new ArrayDeque<>(processes.stream()
                .sorted(Comparator.comparingInt(Process::arrivalTime).thenComparingInt(Process::pid))
                .toList());
    }

    boolean hasPending() {
        return !pending.isEmpty();
    }

    /** Arrival time of the next process that has not been admitted yet. */
    int nextArrivalTime() {
        return pending.getFirst().arrivalTime();
    }

    /** Hands every process with {@code arrivalTime <= time} to {@code sink}. */
    void admitUpTo(int time, Consumer<Process> sink) {
        while (!pending.isEmpty() && pending.getFirst().arrivalTime() <= time) {
            sink.accept(pending.removeFirst());
        }
    }
}
