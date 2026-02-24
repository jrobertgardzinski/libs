package com.jrobertgardzinski.email.usecase.canresetpassword;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.RfcFormatConstraint;
import com.jrobertgardzinski.email.policy.reset.CanResetPassword;
import com.jrobertgardzinski.email.policy.reset.CannotResetPasswordException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanResetPasswordRules {

    private final EmailFactory emailFactory = new EmailFactory(List.of(), List.of());
    private final CanResetPassword canResetPassword = new CanResetPassword(List.of(new RfcFormatConstraint()));

    private boolean result;

    @When("checking if {string} can reset password")
    public void w(String email) {
        try {
            canResetPassword.accept(emailFactory.create(email));
            result = true;
        } catch (CannotResetPasswordException | InvalidEmailException e) {
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
