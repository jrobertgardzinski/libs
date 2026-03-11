Feature: Email validation composition

  Scenario Outline: Combining multiple constraints
    Given a constraint that <first_constraint>
    And a constraint that <second_constraint>
    When they are combined and validated against an email
    Then validation should <result>
    And it should have <violations_count> violations

    Examples:
      | first_constraint           | second_constraint          | result  | violations_count |
      | fails with "invalid format" | fails with "blocked domain" | fail    | 2                |
      | passes                     | fails with "no MX record"    | fail    | 1                |
      | passes                     | passes                     | succeed | 0                |
