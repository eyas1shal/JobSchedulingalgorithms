package cpusim.strategy;

import cpusim.model.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** First-Come First-Served: non-preemptive, runs processes in arrival order. */
public final class FCFSStrategy implements ISchedulingStrategy {

    @Override
    public String name() {
        return "FCFS";
    }

    @Override
    public List<Process> runSimulation(List<Process> processes) {
        List<Process> work = Workloads.freshCopies(processes);

        List<Process> order = new ArrayList<>(work);
        order.sort(Comparator.comparingInt(Process::arrivalTime).thenComparingInt(Process::pid));

        int time = 0;
        for (Process p : order) {
            time = Math.max(time, p.arrivalTime());   // CPU idles until the process arrives
            p.markStarted(time);
            p.execute(p.burstTime());
            time += p.burstTime();
            p.markFinished(time);
        }
        return work;
    }
}
