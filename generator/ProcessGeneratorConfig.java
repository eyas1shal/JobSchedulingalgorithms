package cpusim.generator;

/**
 * Parameters of the random workload.
 *
 * @param processCount  number of processes per workload
 * @param minBurst      smallest CPU burst (inclusive)
 * @param maxBurst      largest CPU burst (inclusive)
 * @param arrivalSpread process i arrives at {@code i * uniform[0, arrivalSpread]}
 */
public record ProcessGeneratorConfig(int processCount, int minBurst, int maxBurst, int arrivalSpread) {

    public ProcessGeneratorConfig {
        if (processCount <= 0) {
            throw new IllegalArgumentException("processCount must be > 0");
        }
        if (minBurst <= 0 || maxBurst < minBurst) {
            throw new IllegalArgumentException("Require 0 < minBurst <= maxBurst");
        }
        if (arrivalSpread < 0) {
            throw new IllegalArgumentException("arrivalSpread must be >= 0");
        }
    }

    /** Same distribution as the original project: 8 processes, bursts 5..99, arrival = i * U[0,10]. */
    public static ProcessGeneratorConfig defaults() {
        return new ProcessGeneratorConfig(8, 5, 99, 10);
    }
}
