package com.jrobertgardzinski.email.domain;

import java.util.LinkedList;
import java.util.List;

public class Validate /* todo <T, List<Specification<T>>>*/ {
    public static void throwOnErrors(Email email, List<EmailSpecification> specifications) {
        List<String> errors = new LinkedList<>();
        for (EmailSpecification s : specifications) {
            s.validate(email).ifInvalid(errors::add);
        }
        if (!errors.isEmpty()) {
            throw new InvalidEmailException(errors);
        }
    }
}

