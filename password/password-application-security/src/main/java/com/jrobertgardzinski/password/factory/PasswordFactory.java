package com.jrobertgardzinski.password.factory;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.password.policy.CanSetPassword;
import com.jrobertgardzinski.password.policy.PasswordPolicyConfig;

public class PasswordFactory {

    private final CanSetPassword canSetPassword;

    public PasswordFactory() {
        this.canSetPassword = new CanSetPassword();
    }

    public PasswordFactory(PasswordPolicyConfig config) {
        this.canSetPassword = new CanSetPassword(config);
    }

    public PlaintextPassword create(String rawPassword) {
        PlaintextPassword candidate = PlaintextPassword.of(rawPassword);
        CanSetPassword.Decision decision = canSetPassword.evaluate(candidate);
        if (decision instanceof CanSetPassword.Decision.Rejected rejected) {
            throw new IllegalArgumentException(rejected.errorCodes().toString());
        }
        return candidate;
    }
}
