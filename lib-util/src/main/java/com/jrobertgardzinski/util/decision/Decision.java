package com.jrobertgardzinski.util.decision;

import com.jrobertgardzinski.util.constraint.ConstraintResult;

import java.util.List;

public interface Decision {

    boolean isAllowed();
    boolean isRejected();

    default List<ConstraintResult> violations() { return List.of(); }

    static Decision allowed() {
        return new _Allowed();
    }

    static Decision rejected(List<ConstraintResult> violations) {
        return new _Rejected(violations);
    }
}
