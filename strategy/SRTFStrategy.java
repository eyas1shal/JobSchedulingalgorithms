package cpusim.strategy;

import cpusim.model.Process;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Shortest Remaining Time First: preemptive. The CPU always runs the ready process with the least
 * remaining time; a newly arrived process can preempt the running one.
 *
 * <p>Event-driven: the clock jumps straight to the next decision point (completion or arrival).
 */
public final class SRTFStrategy implements ISchedulingStrategy {

    private static final Comparator<Process> SHORTEST_REMAINING_FIRST =
            Comparator.comparingInt(Process::remainingTime)
                    .thenComparingInt(Process::arrivalTime)
                    .thenComparingInt(Process::pid);

    @Override
    public String name() {
        return "SRTF";
    }

    @Override
    public List<Process> runSimulation(List<Process> processes) {
        List<Process> work = Workloads.freshCopies(processes);
        ArrivalQueue arrivals = new ArrivalQueue(work);
        PriorityQueue<Process> ready = new PriorityQueue<>(SHORTEST_REMAINING_FIRST);

        int time = 0;
        while (arrivals.hasPending() || !ready.isEmpty()) {
            arrivals.admitUpTo(time, ready::add);
            if (ready.isEmpty()) {
                time = arrivals.nextArrivalTime();       // idle gap
                continue;
            }

            Process current = ready.poll();              // removed before mutation => heap stays valid
            current.markStarted(time);

            int slice = current.remainingTime();
            if (arrivals.hasPending()) {
                slice = Math.min(slice, arrivals.nextArrivalTime() - time);   // re-evaluate at next arrival
            }
            current.execute(slice);
            time += slice;

            if (current.remainingTime() == 0) {
                current.markFinished(time);
            } else {
                ready.add(current);
            }
        }
        return work;
    }
}
