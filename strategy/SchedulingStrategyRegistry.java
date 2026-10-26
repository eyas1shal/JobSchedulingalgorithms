package cpusim.strategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Registry of scheduling algorithms. Adding an algorithm is a single {@link #register} call at the
 * composition root; neither the runner nor the UI needs to change because they only see
 * {@link IStrategyCatalog} and {@link ISchedulingStrategyFactory}.
 *
 * <p>Populate once at start-up; the registry itself is not thread-safe.
 */
public final class SchedulingStrategyRegistry implements ISchedulingStrategyFactory, IStrategyCatalog {

    private record Entry(StrategyDescriptor descriptor, Supplier<? extends ISchedulingStrategy> supplier) { }

    private final Map<String, Entry> entries = new LinkedHashMap<>();

    public SchedulingStrategyRegistry register(String key, Supplier<? extends ISchedulingStrategy> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        String normalized = normalize(key);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        if (entries.containsKey(normalized)) {
            throw new IllegalArgumentException("Strategy already registered: " + normalized);
        }
        String displayName = supplier.get().name();
        entries.put(normalized, new Entry(new StrategyDescriptor(normalized, displayName), supplier));
        return this;
    }

    @Override
    public Optional<ISchedulingStrategy> create(String key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entries.get(normalize(key)))
                .<ISchedulingStrategy>map(entry -> entry.supplier().get());
    }

    @Override
    public List<StrategyDescriptor> descriptors() {
        return entries.values().stream().map(Entry::descriptor).toList();
    }

    private static String normalize(String key) {
        return Objects.requireNonNull(key, "key").trim().toLowerCase(Locale.ROOT);
    }
}
