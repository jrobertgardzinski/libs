package com.jrobertgardzinski.password.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaintextPasswordTest {

    @Test
    void validPasswordSucceeds() {
        assertDoesNotThrow(() -> PlaintextPassword.of("hello"));
    }

    @Test
    void blankPasswordThrows() {
        assertThrows(IllegalArgumentException.class, () -> PlaintextPassword.of("   "));
    }

    @Test
    void equalsSameValue() {
        PlaintextPassword a = PlaintextPassword.of("hello");
        PlaintextPassword b = PlaintextPassword.of("hello");
        assertEquals(a, b);
    }

    @Test
    void notEqualsDifferentValue() {
        PlaintextPassword a = PlaintextPassword.of("hello");
        PlaintextPassword b = PlaintextPassword.of("world");
        assertNotEquals(a, b);
    }

    @Test
    void toStringRedactsValue() {
        PlaintextPassword p = PlaintextPassword.of("hello");
        assertFalse(p.toString().contains("hello"));
    }
}
