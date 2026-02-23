package com.jrobertgardzinski.email.domain;

public class GmailNormalizationPolicy implements EmailNormalizationPolicy {

    @Override
    public String normalize(String rawValue) {
        if (!isGmail(rawValue)) {
            return rawValue;
        }
        return rawValue.substring(0, rawValue.indexOf("@"))
                .replaceAll("\\+.*", "").replace(".", "") + "@gmail.com";
    }

    private boolean isGmail(String email) {
        return email.endsWith("gmail.com")/* || email.endsWith("googlemail.com")*/;
    }
}
