package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EmailPolicySteps {

    private Email email;
    private final Set<String> disposableDomains = new HashSet<>();
    private final Set<String> blockedDomains = new HashSet<>();
    private final Set<String> companyDomains = new HashSet<>();
    private final Set<String> noMxDomains = new HashSet<>();
    private CanRegister.Decision registrationDecision;
    private CanResetPassword.Decision passwordResetDecision;

    @Given("a valid email {string}")
    public void aValidEmail(String raw) {
        email = Email.of(raw);
    }

    @Given("an email {string}")
    public void anEmail(String raw) {
        email = Email.of(raw);
    }

    @Given("an email with invalid format {string}")
    public void anEmailWithInvalidFormat(String raw) {
        email = Email.of(raw);
    }

    @Given("{string} is a {string} domain")
    public void isATypedDomain(String domain, String type) {
        switch (type) {
            case "disposable"   -> disposableDomains.add(domain);
            case "blacklisted"  -> blockedDomains.add(domain);
            case "company-only" -> companyDomains.add(domain);
            default -> throw new IllegalArgumentException("Unknown domain type: " + type);
        }
    }

    @Given("{string} is configured as a {string} domain")
    public void isConfiguredAsTypedDomain(String domain, String type) {
        if ("-".equals(domain)) return;
        isATypedDomain(domain, type);
    }

    @Given("{string} has no MX record")
    public void hasNoMxRecord(String domain) {
        noMxDomains.add(domain);
    }

    @When("registration eligibility is evaluated")
    public void registrationEligibilityIsEvaluated() {
        registrationDecision = new CanRegister(Set.of(), Set.of(), Set.of(), e -> true)
                .evaluate(email);
    }

    @When("registration eligibility is evaluated with active constraints")
    public void registrationEligibilityIsEvaluatedWithActiveConstraints() {
        MxRecordPort mxPort = e -> !noMxDomains.contains(e.domain().value());
        registrationDecision = new CanRegister(disposableDomains, blockedDomains, companyDomains, mxPort)
                .evaluate(email);
    }

    @When("password reset eligibility is evaluated")
    public void passwordResetEligibilityIsEvaluated() {
        passwordResetDecision = new CanResetPassword(Collections.emptySet()).evaluate(email);
    }

    @When("password reset eligibility is evaluated with extended checks")
    public void passwordResetEligibilityIsEvaluatedWithExtendedChecks() {
        passwordResetDecision = new CanResetPassword(blockedDomains).evaluate(email);
    }

    @Then("registration is allowed")
    public void registrationIsAllowed() {
        assertTrue(registrationDecision instanceof CanRegister.Decision.Allowed
                || registrationDecision instanceof CanRegister.Decision.AllowedWithWarning);
    }

    @Then("registration is rejected with violation {string}")
    public void registrationIsRejectedWithViolation(String code) {
        assertInstanceOf(CanRegister.Decision.Rejected.class, registrationDecision);
        assertTrue(((CanRegister.Decision.Rejected) registrationDecision).errorCodes().contains(code));
    }

    @Then("a warning {string} is raised")
    public void aWarningIsRaised(String code) {
        assertInstanceOf(CanRegister.Decision.AllowedWithWarning.class, registrationDecision);
        assertEquals(code, ((CanRegister.Decision.AllowedWithWarning) registrationDecision).code());
    }

    @Then("password reset is allowed")
    public void passwordResetIsAllowed() {
        assertInstanceOf(CanResetPassword.Decision.Allowed.class, passwordResetDecision);
    }

    @Then("password reset is rejected with violation {string}")
    public void passwordResetIsRejectedWithViolation(String code) {
        assertInstanceOf(CanResetPassword.Decision.Rejected.class, passwordResetDecision);
        assertTrue(((CanResetPassword.Decision.Rejected) passwordResetDecision).errorCodes().contains(code));
    }
}
