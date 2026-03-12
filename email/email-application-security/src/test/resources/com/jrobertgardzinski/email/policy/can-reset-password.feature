Feature: Password reset eligibility

  Scenario: Valid email can request a password reset
    Given a valid email "user@example.com"
    When password reset eligibility is evaluated
    Then password reset is allowed

  Scenario: Malformed email cannot request a password reset
    Given an email with invalid format "user space@example.com"
    When password reset eligibility is evaluated
    Then password reset is rejected with violation "RFC_FORMAT_INVALID"

  Scenario: Email from a blacklisted domain cannot request a password reset
    Given a valid email "user@spammer.com"
    And "spammer.com" is a blacklisted domain
    When password reset eligibility is evaluated with extended checks
    Then password reset is rejected with violation "DOMAIN_BLOCKED"
