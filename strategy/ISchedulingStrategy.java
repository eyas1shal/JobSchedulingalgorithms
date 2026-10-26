package cpusim.strategy;

import cpusim.model.Process;

import java.util.List;

/**
 * A CPU scheduling policy.
 *
 * <h2>Contract (every implementation must honour all of it)</h2>
 * <ul>
 *   <li>The input list and its {@link Process} objects are <b>never mutated</b>. The strategy works on
 *       fresh copies, so the same workload can be replayed through different strategies.</li>
 *   <li>The returned list has the <b>same size and order</b> as the input (matching pids), and every
 *       element is finished: {@code startTime >= arrivalTime} and
 *       {@code finishTime >= startTime + burstTime}.</li>
 *   <li>An empty input yields an empty result; a {@code null} input (or null element) throws
 *       {@link NullPointerException}.</li>
 *   <li>Implementations are stateless between calls and therefore safe to reuse and to share.</li>
 * </ul>
 */
public interface ISchedulingStrategy {

    /** Human-readable algorithm name, e.g. {@code "RR (q=20)"}. */
    String name();

    List<Process> runSimulation(List<Process> processes);
}
