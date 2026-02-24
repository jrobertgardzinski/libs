package com.jrobertgardzinski.external;

import com.jrobertgardzinski.email.domain.Email;

public interface MxRecordPort {
    boolean hasMxRecord(Email email);
}