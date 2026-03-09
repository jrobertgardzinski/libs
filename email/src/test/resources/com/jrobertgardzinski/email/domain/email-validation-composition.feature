Feature: Email validation composition

  Scenario: Combining multiple constraints collects all violations
    Given a constraint that fails with "invalid format"
    And a constraint that fails with "blocked domain"
    When they are combined and validated against an email
    Then validation should fail
    And it should contain the violation "invalid format"
    And it should contain the violation "blocked domain"

  Scenario: Combining passing and failing constraints
    Given a constraint that passes
    And a constraint that fails with "no MX record"
    When they are combined and validated against an email
    Then validation should fail
    And it should contain the violation "no MX record"
    But it should NOT contain any other violations

  Scenario: All constraints passing
    Given a constraint that passes
    And another constraint that passes
    When they are combined and validated against an email
    Then validation should succeed
