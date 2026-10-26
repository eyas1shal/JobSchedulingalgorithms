package cpusim.generator;

import cpusim.model.Process;

import java.util.List;

/** Produces a fresh workload. Every call must return brand-new, unstarted {@link Process} objects. */
public interface IProcessGenerator {

    List<Process> generate();
}
