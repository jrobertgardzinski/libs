Feature: Specification AND composition collects all violations

    Rule: when both specifications fail, all violations are collected

        Example: both fail
            Given a specification that always fails with "spec A not satisfied"
            And a specification that always fails with "spec B not satisfied"
            When composed with AND and validated
            Then validation fails
            And violations contain "spec A not satisfied"
            And violations contain "spec B not satisfied"

    Rule: when only one specification fails, only its violation is reported

        Example: first fails, second passes
            Given a specification that always fails with "spec A not satisfied"
            And a specification that always passes
            When composed with AND and validated
            Then validation fails
            And violations contain "spec A not satisfied"

        Example: first passes, second fails
            Given a specification that always passes
            And a specification that always fails with "spec B not satisfied"
            When composed with AND and validated
            Then validation fails
            And violations contain "spec B not satisfied"

    Rule: when all specifications pass, validation succeeds

        Example: both pass
            Given a specification that always passes
            And a specification that always passes
            When composed with AND and validated
            Then validation passes
