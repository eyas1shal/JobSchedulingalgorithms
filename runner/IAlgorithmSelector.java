package cpusim.runner;

import cpusim.strategy.StrategyDescriptor;

import java.util.List;
import java.util.Optional;

/** Lets the user pick one of the offered algorithms. */
public interface IAlgorithmSelector {

    /** @return the chosen descriptor key, or empty when the user wants to quit */
    Optional<String> select(List<StrategyDescriptor> options);
}
