package com.jrobertgardzinski.email.policy;

record _RegistrationAllowed() implements RegistrationDecision {

    public boolean isAllowed() { return true; }
    public boolean isRejected() { return false; }
}
