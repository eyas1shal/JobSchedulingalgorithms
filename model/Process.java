package cpusim.model;

/**
 * Process Control Block (PCB) model.
 *
 * <p>Identity ({@code pid}, {@code arrivalTime}, {@code burstTime}) is immutable. Runtime state
 * (remaining / start / finish time) is only changed through intention-revealing methods that
 * guard their own invariants. This class knows nothing about scheduling policies, random
 * generation or metrics aggregation.
 */
public final class Process {

    public static final int UNSET = -1;

    private final int pid;
    private final int arrivalTime;
    private final int burstTime;

    private int remainingTime;
    private int startTime = UNSET;
    private int finishTime = UNSET;

    public Process(int pid, int arrivalTime, int burstTime) {
        if (pid < 0) {
            throw new IllegalArgumentException("pid must be >= 0 but was " + pid);
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("arrivalTime must be >= 0 but was " + arrivalTime);
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("burstTime must be > 0 but was " + burstTime);
        }
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    // ---- identity -------------------------------------------------------------------------

    public int pid() {
        return pid;
    }

    public int arrivalTime() {
        return arrivalTime;
    }

    public int burstTime() {
        return burstTime;
    }

    // ---- runtime state --------------------------------------------------------------------

    public int remainingTime() {
        return remainingTime;
    }

    public int startTime() {
        return startTime;
    }

    public int finishTime() {
        return finishTime;
    }

    public boolean hasStarted() {
        return startTime != UNSET;
    }

    public boolean isFinished() {
        return finishTime != UNSET;
    }

    /** Records the first time this process got the CPU. Subsequent calls are ignored. */
    public void markStarted(int time) {
        if (startTime == UNSET) {
            startTime = time;
        }
    }

    /** Consumes {@code units} of CPU time from the remaining burst. */
    public void execute(int units) {
        if (units <= 0 || units > remainingTime) {
            throw new IllegalArgumentException(
                    "Cannot execute " + units + " units with " + remainingTime + " remaining (pid " + pid + ")");
        }
        remainingTime -= units;
    }

    /** Records completion. Only legal once the whole burst has been executed. */
    public void markFinished(int time) {
        if (remainingTime != 0) {
            throw new IllegalStateException("Process " + pid + " still has " + remainingTime + " units left");
        }
        finishTime = time;
    }

    // ---- derived values -------------------------------------------------------------------

    public int turnaroundTime() {
        requireFinished();
        return finishTime - arrivalTime;
    }

    public int waitingTime() {
        return turnaroundTime() - burstTime;
    }

    /** Returns a brand-new PCB with the same identity and pristine runtime state. */
    public Process copy() {
        return new Process(pid, arrivalTime, burstTime);
    }

    private void requireFinished() {
        if (!isFinished()) {
            throw new IllegalStateException("Process " + pid + " has not finished yet");
        }
    }

    @Override
    public String toString() {
        return "P" + pid + "[arrival=" + arrivalTime + ", burst=" + burstTime
                + ", remaining=" + remainingTime + ", start=" + startTime + ", finish=" + finishTime + "]";
    }
}
