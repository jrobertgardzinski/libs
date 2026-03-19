package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinLengthConstraintTest {

    @Test
    void constructorRejectsTooSmallMinLength() {
        assertThrows(IllegalArgumentException.class, () -> new _MinLengthConstraint(4));
    }

    @Test
    void constructorAcceptsMinLengthOfFive() {
        assertDoesNotThrow(() -> new _MinLengthConstraint(5));
    }

    @Test
    void passwordMeetingLengthSatisfied() {
        _MinLengthConstraint c = new _MinLengthConstraint(8);
        assertTrue(c.isSatisfied(PlaintextPassword.of("12345678")));
    }

    @Test
    void passwordBelowLengthFails() {
        _MinLengthConstraint c = new _MinLengthConstraint(8);
        assertFalse(c.isSatisfied(PlaintextPassword.of("short")));
        assertEquals("MIN_LENGTH_NOT_MET", c.code());
    }
}
