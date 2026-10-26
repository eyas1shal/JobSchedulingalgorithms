package cpusim.metrics;

import cpusim.model.Process;

import java.util.List;
import java.util.Objects;

/** Average turnaround time (ATT) and average waiting time (AWT). */
public final class MetricsCalculator implements IMetricsCalculator {

    @Override
    public RunMetrics calculate(List<Process> completed) {
        Objects.requireNonNull(completed, "completed");
        if (completed.isEmpty()) {
            throw new IllegalArgumentException("Cannot compute metrics for an empty process list");
        }
        long turnaroundSum = 0;
        long waitingSum = 0;
        for (Process p : completed) {
            if (!p.isFinished()) {
                throw new IllegalArgumentException("Process " + p.pid() + " has not finished");
            }
            turnaroundSum += p.turnaroundTime();
            waitingSum += p.waitingTime();
        }
        double n = completed.size();
        return new RunMetrics(turnaroundSum / n, waitingSum / n);
    }
}
