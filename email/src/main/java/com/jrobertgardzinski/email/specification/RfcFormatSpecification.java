package com.jrobertgardzinski.email.specification;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailSpecification;
import com.jrobertgardzinski.email.domain.ValidationResult;

import java.util.regex.Pattern;

public class RfcFormatSpecification implements EmailSpecification {

    private static final Pattern RFC_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
            "@" +
            "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
            "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    @Override
    public ValidationResult validate(Email candidate) {
        if (!RFC_PATTERN.matcher(candidate.value()).matches()) {
            return ValidationResult.failure("The email " + candidate + " does not comply with RFC 5321/5322 format");
        }
        return ValidationResult.ok();
    }
}
