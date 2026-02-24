package com.jrobertgardzinski.email.usecase.canregister;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.BlockedDomainConstraint;
import com.jrobertgardzinski.email.constraint.DisposableEmailConstraint;
import com.jrobertgardzinski.email.constraint.RfcFormatConstraint;
import com.jrobertgardzinski.email.policy.register.CanRegister;
import com.jrobertgardzinski.email.policy.register.CannotBeRegisteredException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanRegisterRules {

    private final EmailFactory emailFactory = new EmailFactory(List.of(), List.of());
    private final CanRegister canRegister = new CanRegister(List.of(
            new RfcFormatConstraint(),
            new DisposableEmailConstraint(Set.of("mailinator.com", "guerrillamail.com", "tempmail.com")),
            new BlockedDomainConstraint(Set.of("evil.com"))
    ));

    private boolean result;

    @When("checking if {string} can register")
    public void w(String email) {
        try {
            canRegister.accept(emailFactory.create(email));
            result = true;
        } catch (CannotBeRegisteredException | InvalidEmailException e) {
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
