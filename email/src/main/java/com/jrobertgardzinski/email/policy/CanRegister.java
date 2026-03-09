package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.Validate;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanRegister implements Consumer<Email> {

    private final List<Constraint<Email>> constraints;

    private CanRegister(Builder builder) {
        this.constraints = List.copyOf(builder.constraints);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<Constraint<Email>> constraints = new LinkedList<>();

        private Builder() {
            constraints.add(new _RfcFormatConstraint());
        }

        public Builder blockDisposable(Set<String> disposableDomains) {
            constraints.add(new _DisposableEmailConstraint(disposableDomains));
            return this;
        }

        public Builder blockDomains(Set<String> blockedDomains) {
            constraints.add(new _BlockedDomainConstraint(blockedDomains));
            return this;
        }

        public Builder requireCompanyDomain(Set<String> companyDomains) {
            constraints.add(new _IsEmployeeConstraint(companyDomains));
            return this;
        }

        public Builder requireMxRecord(MxRecordPort mxRecordPort) {
            constraints.add(new _MxRecordConstraint(mxRecordPort));
            return this;
        }

        public CanRegister build() {
            return new CanRegister(this);
        }
    }

    @Override
    public void accept(Email email) {
        Validate.throwOnErrors(email, constraints, CannotBeRegisteredException::new);
    }
}
