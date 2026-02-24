package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.util.ValidationResult;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpecificationAndCompositionRules {

    private final List<EmailSpecification> specs = new ArrayList<>();
    private ValidationResult result;

    @Given("a specification that always fails with {string}")
    public void givenFailing(String message) {
        specs.add(email -> ValidationResult.failure(message));
    }

    @Given("a specification that always passes")
    public void givenPassing() {
        specs.add(email -> ValidationResult.ok());
    }

    @When("composed with AND and validated")
    public void whenComposedAndValidated() {
        EmailSpecification composite = specs.stream()
                .reduce(EmailSpecification::and)
                .orElseThrow();
        result = composite.validate(Email.of("user@example.com"));
    }

    @Then("validation fails")
    public void thenFails() {
        assertFalse(result.isValid());
    }

    @Then("validation passes")
    public void thenPasses() {
        assertTrue(result.isValid());
    }

    @And("violations contain {string}")
    public void thenViolationsContain(String violation) {
        assertTrue(result.violations().contains(violation));
    }
}
