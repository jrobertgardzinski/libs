package com.jrobertgardzinski.password.feature;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.password.policy.CanSetPassword;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationRules {

    private CanSetPassword canSetPassword;
    private CanSetPassword.Decision decision;

    @Given("the default password policy is active")
    public void theDefaultPasswordPolicyIsActive() {
        canSetPassword = new CanSetPassword();
    }

    @When("the user provides password {string}")
    public void theUserProvidesPassword(String raw) {
        decision = canSetPassword.evaluate(PlaintextPassword.of(raw));
    }

    @Then("the password is accepted")
    public void thePasswordIsAccepted() {
        assertInstanceOf(CanSetPassword.Decision.Allowed.class, decision);
    }

    @Then("the password is rejected with an error containing {string}")
    public void thePasswordIsRejectedWithAnErrorContaining(String fragment) {
        assertInstanceOf(CanSetPassword.Decision.Rejected.class, decision);
        CanSetPassword.Decision.Rejected rejected = (CanSetPassword.Decision.Rejected) decision;
        assertTrue(
                rejected.errorCodes().stream().anyMatch(code -> code.contains(fragment)),
                String.format("Expected error code containing '%s' but got: %s", fragment, rejected.errorCodes())
        );
    }
}
