package com.jrobertgardzinski.email.usecase.istrusteduser;

import com.jrobertgardzinski.email.domain.EmailFactory;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.email.constraint.IsTrustedUserConstraint;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsTrustedUserRules {

    private final EmailFactory factory = new EmailFactory(new IsTrustedUserConstraint(
            Set.of("vip@gmail.com", "ceo@random.com"),
            Set.of("partner.com", "trusted-corp.pl")
    ));

    private boolean result;

    @When("checking if {string} is a trusted user")
    public void w(String email) {
        try {
            factory.create(email);
            result = true;
        } catch (InvalidEmailException e) {
            result = false;
        }
    }

    @Then("it is trusted")
    public void trusted() {
        assertTrue(result);
    }

    @Then("it is not trusted")
    public void notTrusted() {
        assertFalse(result);
    }
}
