package com.jrobertgardzinski.email.domain;

class _GmailNormalization implements _EmailNormalization {

    @Override
    public String normalize(String localPart, String domain) {
        if (!domain.equals("gmail.com") && !domain.equals("googlemail.com")) {
            return localPart;
        }
        return localPart.replaceAll("\\+.*", "").replace(".", "");
    }
}
