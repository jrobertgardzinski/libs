package com.jrobertgardzinski.hash.algorithm.argon2;

import com.jrobertgardzinski.password.domain.HashAlgorithmPort;
import com.jrobertgardzinski.password.domain.PasswordHash;
import com.jrobertgardzinski.password.domain.PlaintextPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class HashAlgorithmPortTest {

    protected abstract HashAlgorithmPort hashAlgorithm();

    private PasswordHash hash;

    @BeforeEach
    void init() {
        hash = hashAlgorithm().hash(PlaintextPassword.of("StrongPassword1!"));
    }

    @Test
    void correctPasswordVerifies() {
        assertTrue(hashAlgorithm().verify(hash, PlaintextPassword.of("StrongPassword1!")));
    }

    @Test
    void wrongPasswordDoesNotVerify() {
        assertFalse(hashAlgorithm().verify(hash, PlaintextPassword.of("StrongPassword2!")));
    }
}
