Feature: Registration eligibility

  Scenario: Valid email is allowed to register
    Given a valid email "user@example.com"
    When registration eligibility is evaluated
    Then registration is allowed

  Scenario: Malformed email cannot register
    Given an email with invalid format "user space@example.com"
    When registration eligibility is evaluated
    Then registration is rejected with violation "RFC_FORMAT_INVALID"

  Scenario Outline: Email is rejected based on domain policy
    Given a valid email "<email>"
    And "<domain>" is a <domain_type> domain
    When registration eligibility is evaluated with extended checks
    Then registration is rejected with violation "<violation>"

    Examples:
      | email               | domain         | domain_type  | violation             |
      | user@mailinator.com | mailinator.com | disposable   | DISPOSABLE_DOMAIN     |
      | user@spammer.com    | spammer.com    | blacklisted  | DOMAIN_BLOCKED        |
      | user@gmail.com      | acme.com       | company-only | NOT_A_COMPANY_DOMAIN  |
