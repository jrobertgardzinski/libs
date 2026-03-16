package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;

public sealed interface PolicyEvent<D> {
    record Violation<D>(ConstraintResult result) implements PolicyEvent<D> {}
    record Warning<D>(ConstraintResult result)   implements PolicyEvent<D> {}
    record Outcome<D>(D value)                   implements PolicyEvent<D> {}
}
