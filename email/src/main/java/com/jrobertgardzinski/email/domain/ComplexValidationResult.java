package com.jrobertgardzinski.email.domain;

import java.util.List;

public record ComplexValidationResult (List<String> errors) {}