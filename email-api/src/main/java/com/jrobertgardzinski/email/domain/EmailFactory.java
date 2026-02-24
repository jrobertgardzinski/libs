package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.Validate;

import java.util.List;

public class EmailFactory {

    private final List<EmailNormalizationPolicy> normalizationPolicies;
    private final List<? extends Constraint<Email>> specifications;

    public EmailFactory(Constraint<Email> specification) {
        this(List.of(), List.of(specification));
    }

    public EmailFactory(List<EmailNormalizationPolicy> normalizationPolicies, List<? extends Constraint<Email>> specifications) {
        this.normalizationPolicies = normalizationPolicies;
        this.specifications = specifications;
    }

    public Email create(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidEmailException(List.of("Email cannot be null or blank"));
        }

        int atIndex = raw.indexOf('@');
        if (atIndex < 1 || atIndex == raw.length() - 1) {
            throw new InvalidEmailException(List.of("Invalid format: missing '@' or empty parts"));
        }

        String normalizedRaw = raw;
        for (EmailNormalizationPolicy policy : normalizationPolicies) {
            normalizedRaw = policy.normalize(normalizedRaw);
        }

        int normalizedAtIndex = normalizedRaw.indexOf('@');
        LocalPart local = LocalPart.of(normalizedRaw.substring(0, normalizedAtIndex));
        DomainPart domain = DomainPart.of(normalizedRaw.substring(normalizedAtIndex + 1));
        Email email = new Email(local, domain);

        Validate.throwOnErrors(
                email,
                specifications,
                InvalidEmailException::new
        );

        return email;
    }
}
