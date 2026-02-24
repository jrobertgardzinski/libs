package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailNormalizationPolicy;

public class GmailNormalizationPolicy implements EmailNormalizationPolicy {

    @Override
    public String normalize(String rawValue) {
        if (!isGmail(rawValue)) {
            return rawValue;
        }
        return rawValue.substring(0, rawValue.indexOf("@"))
                .replaceAll("\\+.*", "").replace(".", "") + "@gmail.com";
    }

    public Email normalize(Email email) {
        String domain = email.domain().value();
        if (!domain.equals("gmail.com") && !domain.equals("googlemail.com")) {
            return email;
        }
        String local = email.local().value()
                .replaceAll("\\+.*", "")
                .replace(".", "");
        return Email.of(local + "@gmail.com");
    }

    private boolean isGmail(String email) {
        String lower = email.toLowerCase();
        return lower.endsWith("gmail.com") || lower.endsWith("googlemail.com");
    }
}
