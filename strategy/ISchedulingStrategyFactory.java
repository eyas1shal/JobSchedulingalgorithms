package cpusim.strategy;

import java.util.Optional;

/** Creates a strategy from the key advertised in {@link IStrategyCatalog}. */
public interface ISchedulingStrategyFactory {

    Optional<ISchedulingStrategy> create(String key);
}
