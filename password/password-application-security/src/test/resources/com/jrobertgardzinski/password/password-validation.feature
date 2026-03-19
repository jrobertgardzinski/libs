Feature: Password Validation

  Background:
    Given the default password policy is active

  Scenario Outline: Password validation rules
    When the user provides password "<password>"
    Then the password is <result>

    Examples:
      | password     | result                                                    |
      | Str0ng#Pass! | accepted                                                  |
      | Sh0rt#       | rejected with an error containing "MIN_LENGTH_NOT_MET"    |
      | str0ng#pass! | rejected with an error containing "UPPERCASE_REQUIRED"    |
      | Strong#Pass! | rejected with an error containing "DIGIT_REQUIRED"        |
      | Str0ngPass1  | rejected with an error containing "SPECIAL_CHAR_REQUIRED" |
      | STR0NG#PASS! | rejected with an error containing "LOWERCASE_REQUIRED"    |
