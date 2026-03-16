package com.jrobertgardzinski.email.policy;

record _PasswordResetAllowed() implements PasswordResetDecision {

    public boolean isAllowed() { return true; }
    public boolean isRejected() { return false; }
}
