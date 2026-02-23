package com.jrobertgardzinski.email.usecase.canregister;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.domain.CanRegister;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanRegisterRules {

    private final EmailFactory factory = new EmailFactory(new CanRegister(
            Set.of("mailinator.com", "guerrillamail.com", "tempmail.com"),
            Set.of("evil.com")
    ));

    private boolean result;

    @When("checking if {string} can register")
    public void w(String email) {
        try {
            factory.create(email);
            result = true;
        } catch (InvalidEmailException e) {
            result = false;
        }
    }

    @Then("registration is allowed")
    public void allowed() {
        assertTrue(result);
    }

    @Then("registration is rejected")
    public void rejected() {
        assertFalse(result);
    }
}
