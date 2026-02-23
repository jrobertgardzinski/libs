package com.jrobertgardzinski.email.usecase.isemployee;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.domain.IsEmployee;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsEmployeeRules {

    private final EmailFactory factory = new EmailFactory(new IsEmployee(Set.of("acme.com", "acme.pl")));

    private boolean result;

    @When("checking if {string} is an employee")
    public void w(String email) {
        try {
            factory.create(email);
            result = true;
        } catch (InvalidEmailException e) {
            result = false;
        }
    }

    @Then("it is confirmed")
    public void confirmed() {
        assertTrue(result);
    }

    @Then("it is not confirmed")
    public void notConfirmed() {
        assertFalse(result);
    }
}
