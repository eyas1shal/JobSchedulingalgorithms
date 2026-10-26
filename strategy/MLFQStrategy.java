package cpusim.strategy;

import cpusim.model.Process;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Multilevel Feedback Queue.
 *
 * <p>Levels {@code 0..n-1} are Round Robin with the given quanta; the final level is FCFS
 * (run to completion). Default is {@code RR(10) -> RR(50) -> FCFS}, matching the original project.
 *
 * <ul>
 *   <li>All new processes enter level 0.</li>
 *   <li>The CPU always serves the highest-priority non-empty level.</li>
 *   <li>A process that uses its whole quantum is demoted one level.</li>
 *   <li>A process in a lower level is preempted when a new process arrives (which enters level 0);
 *       it resumes at the head of its own level and is not demoted.</li>
 * </ul>
 */
public final class MLFQStrategy implements ISchedulingStrategy {

    private static final List<Integer> DEFAULT_QUANTA = List.of(10, 50);

    private final List<Integer> quanta;

    public MLFQStrategy() {
        this(DEFAULT_QUANTA);
    }

    /** @param quanta time quantum for each RR level, highest priority first; an FCFS level is appended */
    public MLFQStrategy(List<Integer> quanta) {
        Objects.requireNonNull(quanta, "quanta");
        for (Integer q : quanta) {
            if (q == null || q <= 0) {
                throw new IllegalArgumentException("Every quantum must be > 0 but got " + q);
            }
        }
        this.quanta = List.copyOf(quanta);
    }

    @Override
    public String name() {
        String levels = quanta.stream().map(q -> "RR " + q).collect(Collectors.joining(" -> "));
        return "MLFQ (" + (levels.isEmpty() ? "" : levels + " -> ") + "FCFS)";
    }

    @Override
    public List<Process> runSimulation(List<Process> processes) {
        List<Process> work = Workloads.freshCopies(processes);
        ArrivalQueue arrivals = new ArrivalQueue(work);

        int levelCount = quanta.size() + 1;               // last level = FCFS
        int fcfsLevel = levelCount - 1;
        List<Deque<Process>> queues = new ArrayList<>(levelCount);
        for (int i = 0; i < levelCount; i++) {
            queues.add(new ArrayDeque<>());
        }
        Deque<Process> entryQueue = queues.get(0);

        int time = 0;
        while (arrivals.hasPending() || anyWaiting(queues)) {
            arrivals.admitUpTo(time, entryQueue::addLast);

            int level = highestNonEmptyLevel(queues);
            if (level < 0) {
                time = arrivals.nextArrivalTime();         // idle gap
                continue;
            }

            Process current = queues.get(level).pollFirst();
            current.markStarted(time);

            int budget = level < fcfsLevel ? quanta.get(level) : Integer.MAX_VALUE;
            int slice = Math.min(budget, current.remainingTime());
            if (level > 0 && arrivals.hasPending()) {
                slice = Math.min(slice, arrivals.nextArrivalTime() - time);   // possible preemption point
            }
            current.execute(slice);
            time += slice;
            arrivals.admitUpTo(time, entryQueue::addLast);

            if (current.remainingTime() == 0) {
                current.markFinished(time);
            } else if (slice == budget) {
                queues.get(level + 1).addLast(current);    // used full quantum => demote (never FCFS level)
            } else {
                queues.get(level).addFirst(current);       // preempted by a higher-priority arrival
            }
        }
        return work;
    }

    private static boolean anyWaiting(List<Deque<Process>> queues) {
        return highestNonEmptyLevel(queues) >= 0;
    }

    private static int highestNonEmptyLevel(List<Deque<Process>> queues) {
        for (int i = 0; i < queues.size(); i++) {
            if (!queues.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
