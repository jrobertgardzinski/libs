package com.jrobertgardzinski.email.constraint.disposable;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.DisposableEmailConstraint;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisposableEmailConstraintTest {

    private final EmailFactory factory = new EmailFactory(
            new DisposableEmailConstraint(Set.of("mailinator.com", "guerrillamail.com", "tempmail.com")));

    @Test
    void satisfiedWhenNotDisposable() {
        assertDoesNotThrow(() -> factory.create("user@gmail.com"));
    }

    @Test
    void notSatisfiedWhenDisposable() {
        assertThrows(InvalidEmailException.class, () -> factory.create("user@mailinator.com"));
    }

    @Test
    void disposableDomainCaseInsensitive() {
        assertThrows(InvalidEmailException.class, () -> factory.create("user@MAILINATOR.COM"));
    }
}
