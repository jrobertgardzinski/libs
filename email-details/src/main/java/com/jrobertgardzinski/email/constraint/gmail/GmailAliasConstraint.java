package com.jrobertgardzinski.email.constraint.gmail;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

public class GmailAliasConstraint extends EmailConstraint {

    @Override
    public boolean isSatisfied(Email candidate) {
        String domain = candidate.domain().value();
        return domain.equals("gmail.com") || domain.equals("googlemail.com");
    }

    @Override
    public String errorMessage() {
        return "email is not a Gmail address";
    }
}
