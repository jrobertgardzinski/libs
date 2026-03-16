package com.jrobertgardzinski.util.decision;

record _Allowed() implements Decision {

    public boolean isAllowed() { return true; }
    public boolean isRejected() { return false; }
}
