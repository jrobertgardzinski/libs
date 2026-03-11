Feature: Password Validation

  Background:
    Given the default password policy is active

  Scenario Outline: Password validation rules
    When the user provides password "<password>"
    Then the password is <result>

    Examples:
      | password      | result                                              |
      | Str0ng#Pass!  | accepted                                            |
      | Sh0rt#        | rejected with an error containing "characters long" |
      | str0ng#pass!  | rejected with an error containing "uppercase"       |
      | Strong#Pass!  | rejected with an error containing "digit"           |
      | Str0ngPass1   | rejected with an error containing "special"         |
      | STR0NG#PASS!  | rejected with an error containing "lowercase"       |
