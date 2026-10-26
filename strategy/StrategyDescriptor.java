package cpusim.strategy;

/** Lightweight description of a registered algorithm, safe to hand to the UI. */
public record StrategyDescriptor(String key, String displayName) { }
