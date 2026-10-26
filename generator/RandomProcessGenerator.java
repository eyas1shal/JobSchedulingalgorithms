package cpusim.generator;

import cpusim.model.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/** {@link IProcessGenerator} backed by an injected {@link Random} (inject a seeded one for reproducibility). */
public final class RandomProcessGenerator implements IProcessGenerator {

    private final ProcessGeneratorConfig config;
    private final Random random;

    public RandomProcessGenerator(ProcessGeneratorConfig config, Random random) {
        this.config = Objects.requireNonNull(config, "config");
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public List<Process> generate() {
        List<Process> processes = new ArrayList<>(config.processCount());
        int burstRange = config.maxBurst() - config.minBurst() + 1;
        for (int pid = 0; pid < config.processCount(); pid++) {
            int burst = config.minBurst() + random.nextInt(burstRange);
            int arrival = pid * random.nextInt(config.arrivalSpread() + 1);
            processes.add(new Process(pid, arrival, burst));
        }
        return List.copyOf(processes);
    }
}
