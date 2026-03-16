Feature: Registration eligibility

  CanRegister decides whether an email address may be used to create an account.
  It enforces the following constraints:

    Blocking (any violation causes immediate rejection):
      - RFC format        — the address must be syntactically valid per RFC 5321/5322
      - Disposable domain — the domain must not belong to a disposable email provider
      - Blocked domain    — the domain must not be on the blacklist
      - Company-only      — the address must belong to one of the configured company domains

    Warning (non-blocking, evaluated asynchronously):
      - MX record         — the domain should have a mail exchange record

  Scenario: Email satisfies all blocking constraints — registration is allowed
    Given a valid email "user@example.com"
    When registration eligibility is evaluated
    Then registration is allowed

  Scenario Outline: Email violates a blocking constraint — registration is rejected
    Given an email "<email>"
    And "<domain>" is configured as a "<domain_type>" domain
    When registration eligibility is evaluated with active constraints
    Then registration is rejected with violation "<violation>"

    Examples:
      | email               | domain         | domain_type    | violation            |
      | user space@test.com | -              | -              | RFC_FORMAT_INVALID   |
      | user@mailinator.com | mailinator.com | disposable     | DISPOSABLE_DOMAIN    |
      | user@spammer.com    | spammer.com    | blacklisted    | DOMAIN_BLOCKED       |
      | user@gmail.com      | acme.com       | company-only   | NOT_A_COMPANY_DOMAIN |

  Scenario: Domain has no MX record — registration is allowed with a warning
    Given a valid email "user@ghost-domain.com"
    And "ghost-domain.com" has no MX record
    When registration eligibility is evaluated with active constraints
    Then registration is allowed
    And a warning "NO_MX_RECORD" is raised
