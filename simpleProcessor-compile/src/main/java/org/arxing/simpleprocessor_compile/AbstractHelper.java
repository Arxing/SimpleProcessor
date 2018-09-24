package org.arxing.simpleprocessor_compile;

public abstract class AbstractHelper<TSpec, TBuilder> {
    protected TBuilder builder;

    public abstract TSpec create();
}
