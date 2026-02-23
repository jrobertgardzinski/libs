package com.jrobertgardzinski.email.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Email value object.
 *
 * Enforces only structural (syntactic) invariants:
 *   - not blank
 *   - exactly one '@' separating a non-empty local part from a non-empty domain
 *
 * Business rules (RFC compliance, Gmail alias normalisation, domain blacklists,
 * disposable-address detection) live in email-specifications.
 * System decisions (can register, is employee) live in email-usecases.
 */
@ToString
@EqualsAndHashCode
public final class Email {

    private final LocalPart local;
    private final DomainPart domain;

    Email(LocalPart local, DomainPart domain) {
        this.local  = local;
        this.domain = domain;
    }

    public LocalPart local()   { return local;  }
    public DomainPart domain() { return domain; }
    public String value() { return local + "@" + domain; }
}
