package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.Constraint;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import java.util.List;
import java.util.Set;

public class CanRegister {

    private final List<ErrorConstraint<Email>> errorConstraints;
    private final WarningConstraint<Email> warningConstraints;

    public CanRegister(
            Set<String> disposableDomains,
            Set<String> blockedDomains,
            Set<String> companyDomains,
            MxRecordPort mxRecordPort) {
        this.errorConstraints = List.of(
                new _RfcFormatConstraint(),
                new _DisposableEmailConstraint(disposableDomains),
                new _BlockedDomainConstraint(blockedDomains),
                new _IsEmployeeConstraint(companyDomains));
        this.warningConstraints = new _MxRecordConstraint(mxRecordPort);
    }

    public Decision evaluate(Email email) {
        List<String> codes = errorConstraints.stream()
                .filter(el -> !el.isSatisfied(email))
                .map(Constraint::code)
                .toList();

        if (!codes.isEmpty()) {
            return new Decision.Rejected(codes);
        }

        boolean mxRecordExists = warningConstraints.isSatisfied(email);

        return mxRecordExists ?
                new Decision.Allowed() :
                new Decision.AllowedWithWarning(warningConstraints.code());
    }

    public interface Decision {
        record Rejected(
                List<String> errorCodes) implements Decision {
        }

        record Allowed() implements Decision {}

        record AllowedWithWarning(String code) implements Decision {}
    }

}
