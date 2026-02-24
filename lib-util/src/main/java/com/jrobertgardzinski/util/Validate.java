package com.jrobertgardzinski.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public final class Validate {

    private Validate() {}

    public static <T, X extends RuntimeException> void throwOnErrors(
            T candidate,
            Iterable<? extends Constraint<T>> constraints,
            BiFunction<T, List<String>, X> exceptionFactory
    ) {
        List<String> errors = new LinkedList<>();

        for (Constraint<T> c : constraints) {
            c.validate(candidate).ifInvalid(errors::add);
        }

        if (!errors.isEmpty()) {
            throw exceptionFactory.apply(candidate, errors);
        }
    }
}
