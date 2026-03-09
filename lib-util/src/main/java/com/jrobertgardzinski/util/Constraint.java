package com.jrobertgardzinski.util;

public abstract class Constraint<T> implements Specification<T> {

    public abstract boolean isSatisfied(T candidate);
    public abstract String errorMessage();

    public ValidationResult validate(T candidate) {
        return isSatisfied(candidate) ?
                    ValidationResult.ok() :
                    ValidationResult.failure(errorMessage());
    }

    @Override
    final public boolean isSatisfiedBy(T candidate) {
        return isSatisfied(candidate);
    }

    @Override
    final public Specification<T> and(Specification<T> other) {
        return Specification.super.and(other);
    }

    @Override
    final public Specification<T> or(Specification<T> other) {
        return Specification.super.or(other);
    }

    @Override
    final public Specification<T> not(Specification<T> other) {
        return Specification.super.not(other);
    }
}
