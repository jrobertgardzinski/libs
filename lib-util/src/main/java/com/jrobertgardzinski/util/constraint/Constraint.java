package com.jrobertgardzinski.util.constraint;

public abstract class Constraint<T> {

    public abstract boolean isSatisfied(T candidate);
    public abstract String code();
}
