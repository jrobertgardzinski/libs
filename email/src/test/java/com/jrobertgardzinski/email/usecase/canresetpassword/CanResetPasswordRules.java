package com.jrobertgardzinski.email.usecase.canresetpassword;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.domain.CanResetPassword;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanResetPasswordRules {

    private final EmailFactory factory = new EmailFactory(new CanResetPassword());

    private boolean result;

    @When("checking if {string} can reset password")
    public void w(String email) {
        try {
            factory.create(email);
            result = true;
        } catch (InvalidEmailException e) {
            result = false;
        }
    }

    @Then("password reset is allowed")
    public void allowed() {
        assertTrue(result);
    }

    @Then("password reset is rejected")
    public void rejected() {
        assertFalse(result);
    }
}
