package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.ValidationResult;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidationCompositionSteps {

    private final List<Constraint<Email>> constraints = new ArrayList<>();
    private ValidationResult result;

    @Given("a constraint that fails with {string}")
    public void aConstraintThatFailsWith(String message) {
        constraints.add(new Constraint<Email>() {
            @Override public boolean isSatisfied(Email email) { return false; }
            @Override public String errorMessage() { return message; }
        });
    }

    @Given("a constraint that passes")
    @And("another constraint that passes")
    public void aConstraintThatPasses() {
        constraints.add(new Constraint<Email>() {
            @Override public boolean isSatisfied(Email email) { return true; }
            @Override public String errorMessage() { return ""; }
        });
    }

    @When("they are combined and validated against an email")
    public void theyAreCombinedAndValidatedAgainstAnEmail() {
        Email email = Email.of("test@example.com");
        
        // Zbieramy błędy ze wszystkich reguł ręcznie, co symuluje działanie Validate.throwOnErrors
        List<String> allViolations = constraints.stream()
                .map(c -> c.validate(email))
                .flatMap(r -> r.violations().stream())
                .collect(Collectors.toList());
        
        if (allViolations.isEmpty()) {
            result = ValidationResult.ok();
        } else {
            // Składamy błędy w jeden ValidationResult
            result = allViolations.stream()
                    .map(ValidationResult::failure)
                    .reduce(ValidationResult.ok(), ValidationResult::and);
        }
    }

    @Then("validation should fail")
    public void validationShouldFail() {
        assertFalse(result.isValid(), "Validation should have failed");
    }

    @Then("validation should succeed")
    public void validationShouldSucceed() {
        assertTrue(result.isValid(), "Validation should have succeeded");
    }

    @And("it should contain the violation {string}")
    public void itShouldContainTheViolation(String message) {
        assertTrue(result.violations().contains(message), 
                "Expected violation '" + message + "' not found in: " + result.violations());
    }

    @And("it should NOT contain any other violations")
    public void itShouldNotContainAnyOtherViolations() {
        assertEquals(1, result.violations().size(), 
                "Expected exactly one violation, but found: " + result.violations());
    }
}
