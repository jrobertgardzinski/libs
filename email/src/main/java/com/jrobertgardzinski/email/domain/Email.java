package com.jrobertgardzinski.email.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Email value object.
 * <p>
 * Enforces only structural (syntactic) invariants:
 * - not blank
 * - exactly one '@' separating a non-empty local part from a non-empty domain
 * <p>
 * Business rules (RFC compliance, Gmail alias normalisation, domain blacklists,
 * disposable-address detection) live in the domain constraints.
 * System decisions (can register, is employee) live in domain policies.
 */
public final class Email {

    private static final List<_EmailNormalization>
            DEFAULT_NORMALIZATION_POLICIES = List.of(new _GmailNormalization());

    private final LocalPart local;
    private final Optional<LocalPart> normalized;
    private final DomainPart domain;

    Email(LocalPart local, Optional<LocalPart> normalized, DomainPart domain) {
        this.local = local;
        this.normalized = normalized;
        this.domain = domain;
    }

    public static Email of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        int atIndex = raw.indexOf('@');
        if (atIndex < 1 || atIndex != raw.lastIndexOf('@') || atIndex == raw.length() - 1) {
            throw new IllegalArgumentException("Invalid email format: " + raw);
        }

        LocalPart local = LocalPart.of(raw.substring(0, atIndex));
        DomainPart domain = DomainPart.of(raw.substring(atIndex + 1));

        String normalizedRaw = local.value();
        for (_EmailNormalization policy : DEFAULT_NORMALIZATION_POLICIES) {
            normalizedRaw = policy.normalize(normalizedRaw, domain.value());
        }
        Optional<LocalPart> normalized = Objects.equals(normalizedRaw, local.value()) ?
                Optional.empty() :
                Optional.of(LocalPart.of(normalizedRaw));

        return new Email(local, normalized, domain);
    }

    public LocalPart local() {
        return local;
    }

    public Optional<LocalPart> normalized() {
        return normalized;
    }

    public DomainPart domain() {
        return domain;
    }

    public String value() {
        return local + "@" + domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email other = (Email) o;
        return local.equals(other.local) && domain.equals(other.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(local, domain);
    }

    @Override
    public String toString() {
        return value();
    }
}
