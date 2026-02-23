package com.jrobertgardzinski.email.specification.disposable;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.specification.DisposableEmailSpecification;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisposableEmailSpecificationTest {

    private final EmailFactory factory = new EmailFactory(
            new DisposableEmailSpecification(Set.of("mailinator.com", "guerrillamail.com", "tempmail.com")));

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
