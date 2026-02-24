package com.jrobertgardzinski.email.constraint.gmail;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.GmailNormalizationPolicy;
import com.jrobertgardzinski.email.constraint.RfcFormatConstraint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GmailNormalizationPolicyTest {

    private final GmailNormalizationPolicy policy = new GmailNormalizationPolicy();
    private final EmailFactory gmailFactory = new EmailFactory(new GmailAliasConstraint());
    private final EmailFactory rfcFactory   = new EmailFactory(new RfcFormatConstraint());

    @ParameterizedTest
    @ValueSource(strings = {"user@gmail.com", "user@googlemail.com", "user@GMAIL.COM"})
    void satisfiedForGmail(String value) {
        assertDoesNotThrow(() -> gmailFactory.create(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@yahoo.com", "user@home.pl"})
    void notSatisfiedForNonGmail(String value) {
        assertThrows(InvalidEmailException.class, () -> gmailFactory.create(value));
    }

    @Test
    void normalizesDotsAndPlusTags() {
        Email alias    = rfcFactory.create("j.doe+spam@gmail.com");
        Email expected = rfcFactory.create("jdoe@gmail.com");
        assertEquals(expected, policy.normalize(alias));
    }

    @Test
    void normalizesGooglemailToGmail() {
        Email alias    = rfcFactory.create("user@googlemail.com");
        Email expected = rfcFactory.create("user@gmail.com");
        assertEquals(expected, policy.normalize(alias));
    }

    @Test
    void normalizeDoesNotChangeNonGmail() {
        Email email = rfcFactory.create("user@yahoo.com");
        assertSame(email, policy.normalize(email));
    }

    @Test
    void aliasAndCanonicalNormalizeToSame() {
        Email alias1 = rfcFactory.create("j.doe+work@gmail.com");
        Email alias2 = rfcFactory.create("jdoe+personal@gmail.com");
        assertEquals(policy.normalize(alias1), policy.normalize(alias2));
    }
}
