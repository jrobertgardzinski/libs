package com.jrobertgardzinski.password.policy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanSetPasswordConfigTest {

    @Test
    void defaultsAreApplied() {
        CanSetPassword.Config config = CanSetPassword.Config.builder().build();

        assertEquals(12, config.minLength());
        assertTrue(config.requireLowercase());
        assertTrue(config.requireUppercase());
        assertTrue(config.requireDigit());
        assertEquals("#?!", config.specialChars());
    }

    @Test
    void builderOverridesDefaults() {
        CanSetPassword.Config config = CanSetPassword.Config.builder()
                .minLength(8)
                .requireDigit(false)
                .noSpecialChars()
                .build();

        assertEquals(8, config.minLength());
        assertFalse(config.requireDigit());
        assertTrue(config.specialChars().isBlank());
    }
}
