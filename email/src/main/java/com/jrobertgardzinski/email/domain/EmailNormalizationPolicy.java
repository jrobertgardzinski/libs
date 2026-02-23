package com.jrobertgardzinski.email.domain;

public interface EmailNormalizationPolicy {
    String normalize(String rawValue);
}
