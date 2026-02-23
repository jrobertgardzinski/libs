package com.jrobertgardzinski.email.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates Email value objects while enforcing the active policy.
 * Single entry point for producing Email instances in application code.
 */
public class EmailFactory {

    private final List<EmailNormalizationPolicy> normalizationPolicies;
    private final List<EmailSpecification> specifications;

    public EmailFactory(List<EmailNormalizationPolicy> normalizationPolicies, List<EmailSpecification> specifications) {
        this.normalizationPolicies = normalizationPolicies;
        this.specifications = specifications;
    }

    public Email create(String rawValue) {
        List<String> errors = new ArrayList<>();

        if (rawValue == null || rawValue.isBlank()) {
            throw new InvalidEmailException(List.of("Email cannot be null or blank"));
        }

        int atIndex = rawValue.indexOf('@');
        if (atIndex < 1 || atIndex == rawValue.length() - 1) {
            throw new InvalidEmailException(List.of("Invalid format: missing '@' or empty parts"));
        }

        String normalizedRawValue = rawValue;
        for (EmailNormalizationPolicy policy : normalizationPolicies) {
            normalizedRawValue = policy.normalize(normalizedRawValue);
        }

        LocalPart local = new LocalPart(normalizedRawValue.substring(0, atIndex));
        DomainPart domain = new DomainPart(normalizedRawValue.substring(atIndex + 1));
        Email tempEmail = new Email(local, domain);

        Validate.throwOnErrors(tempEmail, specifications);

        return tempEmail;
    }
}
