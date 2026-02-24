package com.jrobertgardzinski.email.constraint.rfc;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.RfcFormatConstraint;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RfcFormatConstraintTest {

    private final EmailFactory factory = new EmailFactory(new RfcFormatConstraint());

    @ParameterizedTest
    @ValueSource(strings = {
            "user@gmail.com",
            "j.doe+spam@gmail.com",
            "user123@home.pl",
            "user@mail.google.com"
    })
    void validEmails(String value) {
        assertDoesNotThrow(() -> factory.create(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user @gmail.com",
            "user@-gmail.com",
            "user@gmail..com"
    })
    void invalidEmails(String value) {
        assertThrows(InvalidEmailException.class, () -> factory.create(value));
    }
}
