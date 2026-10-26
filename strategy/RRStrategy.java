package cpusim.strategy;

import cpusim.model.Process;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Round Robin with a fixed time quantum. Processes that arrive while another is running are queued
 * ahead of the process being preempted (the conventional textbook rule).
 */
public final class RRStrategy implements ISchedulingStrategy {

    private final int timeQuantum;

    public RRStrategy(int timeQuantum) {
        if (timeQuantum <= 0) {
            throw new IllegalArgumentException("timeQuantum must be > 0 but was " + timeQuantum);
        }
        this.timeQuantum = timeQuantum;
    }

    public int timeQuantum() {
        return timeQuantum;
    }

    @Override
    public String name() {
        return "RR (q=" + timeQuantum + ")";
    }

    @Override
    public List<Process> runSimulation(List<Process> processes) {
        List<Process> work = Workloads.freshCopies(processes);
        ArrivalQueue arrivals = new ArrivalQueue(work);
        Deque<Process> ready = new ArrayDeque<>();

        int time = 0;
        while (arrivals.hasPending() || !ready.isEmpty()) {
            arrivals.admitUpTo(time, ready::addLast);
            if (ready.isEmpty()) {
                time = arrivals.nextArrivalTime();       // idle gap
                continue;
            }

            Process current = ready.pollFirst();
            current.markStarted(time);

            int slice = Math.min(timeQuantum, current.remainingTime());
            current.execute(slice);
            time += slice;

            arrivals.admitUpTo(time, ready::addLast);    // arrivals during the slice go first
            if (current.remainingTime() == 0) {
                current.markFinished(time);
            } else {
                ready.addLast(current);
            }
        }
        return work;
    }
}
