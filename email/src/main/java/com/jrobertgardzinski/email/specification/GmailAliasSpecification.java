package com.jrobertgardzinski.email.specification;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailSpecification;
import com.jrobertgardzinski.email.domain.ValidationResult;

public class GmailAliasSpecification implements EmailSpecification {

    @Override
    public ValidationResult validate(Email candidate) {
        String domain = candidate.domain().value();
        if (!domain.equals("gmail.com") && !domain.equals("googlemail.com")) {
            return ValidationResult.failure("The email " + candidate + " is not a Gmail address (gmail.com or googlemail.com)");
        }
        return ValidationResult.ok();
    }
}
