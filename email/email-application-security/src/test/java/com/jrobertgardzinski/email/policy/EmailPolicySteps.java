package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EmailPolicySteps {

    private Email email;
    private final Set<String> disposableDomains = new HashSet<>();
    private final Set<String> blockedDomains = new HashSet<>();
    private final Set<String> companyDomains = new HashSet<>();
    private RegistrationDecision registrationDecision;
    private PasswordResetDecision passwordResetDecision;

    @Given("a valid email {string}")
    public void aValidEmail(String raw) {
        email = Email.of(raw);
    }

    @Given("an email with invalid format {string}")
    public void anEmailWithInvalidFormat(String raw) {
        email = Email.of(raw);
    }

    @Given("{string} is a {string} domain")
    public void isATypedDomain(String domain, String type) {
        switch (type) {
            case "disposable"    -> disposableDomains.add(domain);
            case "blacklisted"   -> blockedDomains.add(domain);
            case "company-only"  -> companyDomains.add(domain);
            default -> throw new IllegalArgumentException("Unknown domain type: " + type);
        }
    }

    @When("registration eligibility is evaluated")
    public void registrationEligibilityIsEvaluated() {
        registrationDecision = CanRegister.minimalistic().evaluate(email);
    }

    @When("registration eligibility is evaluated with extended checks")
    public void registrationEligibilityIsEvaluatedWithExtendedChecks() {
        List<Constraint<Email>> blocking = new ArrayList<>();
        if (!disposableDomains.isEmpty()) blocking.add(new _DisposableEmailConstraint(disposableDomains));
        if (!blockedDomains.isEmpty()) blocking.add(new _BlockedDomainConstraint(blockedDomains));
        if (!companyDomains.isEmpty()) blocking.add(new _IsEmployeeConstraint(companyDomains));
        registrationDecision = CanRegister.custom(blocking, List.of(), null).evaluate(email);
    }

    @When("password reset eligibility is evaluated")
    public void passwordResetEligibilityIsEvaluated() {
        passwordResetDecision = CanResetPassword.minimalistic().evaluate(email);
    }

    @When("password reset eligibility is evaluated with extended checks")
    public void passwordResetEligibilityIsEvaluatedWithExtendedChecks() {
        List<Constraint<Email>> blocking = new ArrayList<>();
        if (!blockedDomains.isEmpty()) blocking.add(new _BlockedDomainConstraint(blockedDomains));
        passwordResetDecision = CanResetPassword.custom(blocking, List.of(), null).evaluate(email);
    }

    @Then("registration is allowed")
    public void registrationIsAllowed() {
        assertTrue(registrationDecision.isAllowed());
    }

    @Then("registration is rejected with violation {string}")
    public void registrationIsRejectedWithViolation(String code) {
        assertTrue(registrationDecision.isRejected());
        assertTrue(registrationDecision.violations().stream().anyMatch(v -> v.code().equals(code)));
    }

    @Then("password reset is allowed")
    public void passwordResetIsAllowed() {
        assertTrue(passwordResetDecision.isAllowed());
    }

    @Then("password reset is rejected with violation {string}")
    public void passwordResetIsRejectedWithViolation(String code) {
        assertTrue(passwordResetDecision.isRejected());
        assertTrue(passwordResetDecision.violations().stream().anyMatch(v -> v.code().equals(code)));
    }
}
