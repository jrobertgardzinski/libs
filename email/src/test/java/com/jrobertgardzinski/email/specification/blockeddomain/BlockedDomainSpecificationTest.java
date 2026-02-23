package com.jrobertgardzinski.email.specification.blockeddomain;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.specification.BlockedDomainSpecification;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlockedDomainSpecificationTest {

    private final EmailFactory factory = new EmailFactory(
            new BlockedDomainSpecification(Set.of("evil.com", "spam.org")));

    @Test
    void satisfiedWhenDomainNotBlocked() {
        assertDoesNotThrow(() -> factory.create("user@gmail.com"));
    }

    @Test
    void notSatisfiedWhenDomainBlocked() {
        assertThrows(InvalidEmailException.class, () -> factory.create("user@evil.com"));
    }

    @Test
    void blockedDomainCaseInsensitive() {
        assertThrows(InvalidEmailException.class, () -> factory.create("user@EVIL.COM"));
    }

    @Test
    void emptyBlocklistAllowsEverything() {
        EmailFactory empty = new EmailFactory(new BlockedDomainSpecification(Set.of()));
        assertDoesNotThrow(() -> empty.create("user@anything.com"));
    }
}
