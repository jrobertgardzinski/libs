package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.WarningConstraint;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class EmailPolicySteps {

    private Email email;
    private final Set<String> disposableDomains = new HashSet<>();
    private final Set<String> blockedDomains = new HashSet<>();
    private final Set<String> companyDomains = new HashSet<>();
    private final Set<String> noMxDomains = new HashSet<>();
    private final List<ConstraintResult> capturedWarnings = new ArrayList<>();
    private CountDownLatch warningLatch;
    private RegistrationDecision registrationDecision;
    private PasswordResetDecision passwordResetDecision;

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
        registrationDecision = CanRegister.minimalistic().evaluate(email);
    }

    @When("registration eligibility is evaluated with active constraints")
    public void registrationEligibilityIsEvaluatedWithActiveConstraints() {
        List<ErrorConstraint<Email>> blocking = new ArrayList<>();
        blocking.add(new _RfcFormatConstraint());
        if (!disposableDomains.isEmpty()) blocking.add(new _DisposableEmailConstraint(disposableDomains));
        if (!blockedDomains.isEmpty()) blocking.add(new _BlockedDomainConstraint(blockedDomains));
        if (!companyDomains.isEmpty()) blocking.add(new _IsEmployeeConstraint(companyDomains));

        List<WarningConstraint<Email>> warnings = new ArrayList<>();
        Consumer<List<ConstraintResult>> warningHandler = null;
        if (!noMxDomains.isEmpty()) {
            MxRecordPort mxPort = e -> !noMxDomains.contains(e.domain().value());
            warnings.add(new _MxRecordConstraint(mxPort));
            warningLatch = new CountDownLatch(1);
            warningHandler = received -> {
                capturedWarnings.addAll(received);
                warningLatch.countDown();
            };
        }

        registrationDecision = CanRegister.custom(blocking, warnings, warningHandler).evaluate(email);
    }

    @When("password reset eligibility is evaluated")
    public void passwordResetEligibilityIsEvaluated() {
        passwordResetDecision = CanResetPassword.minimalistic().evaluate(email);
    }

    @When("password reset eligibility is evaluated with extended checks")
    public void passwordResetEligibilityIsEvaluatedWithExtendedChecks() {
        List<ErrorConstraint<Email>> blocking = new ArrayList<>();
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

    @Then("a warning {string} is raised")
    public void aWarningIsRaised(String code) throws InterruptedException {
        assertTrue(warningLatch.await(5, TimeUnit.SECONDS), "Warning handler was not called within timeout");
        assertTrue(capturedWarnings.stream().anyMatch(w -> w.code().equals(code)));
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
