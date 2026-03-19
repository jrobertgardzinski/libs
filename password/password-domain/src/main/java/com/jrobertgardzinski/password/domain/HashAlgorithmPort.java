package com.jrobertgardzinski.password.domain;

public interface HashAlgorithmPort {
    PasswordHash hash(PlaintextPassword plaintextPassword);
    boolean verify(PasswordHash passwordHash, PlaintextPassword plaintextPassword);
}
