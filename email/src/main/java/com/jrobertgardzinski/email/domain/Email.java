package com.jrobertgardzinski.email.domain;

import java.util.List;
import java.util.Objects;

/**
 * Email value object.
 *
 * Enforces only structural (syntactic) invariants:
 *   - not blank
 *   - exactly one '@' separating a non-empty local part from a non-empty domain
 *
 * Business rules (RFC compliance, Gmail alias normalisation, domain blacklists,
 * disposable-address detection) live in the domain constraints.
 * System decisions (can register, is employee) live in domain policies.
 */
public final class Email {

    private static final List<_EmailNormalization>
            normalizationPolicies = List.of(new _GmailNormalization());

    private final LocalPart local;
    private final LocalPart normalized;
    private final DomainPart domain;

    Email(LocalPart local, LocalPart normalized, DomainPart domain) {
        this.local      = local;
        this.normalized = normalized;
        this.domain     = domain;
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
        for (_EmailNormalization policy : normalizationPolicies) {
            normalizedRaw = policy.normalize(normalizedRaw, domain.value());
        }
        LocalPart normalized = LocalPart.of(normalizedRaw);

        return new Email(local, normalized, domain);
    }

    public LocalPart local() {
        return local;
    }

    public LocalPart normalized() {
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
        if (!(o instanceof Email other)) return false;
        return local.equals(other.local) && domain.equals(other.domain);
    }

    @Override
    public int hashCode() { return Objects.hash(local, domain); }

    @Override
    public String toString() { return value(); }
}
