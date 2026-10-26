package cpusim.strategy;

import java.util.List;

/** Read-only view of the available algorithms, in registration order. */
public interface IStrategyCatalog {

    List<StrategyDescriptor> descriptors();
}
