package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.ConstraintResult;
import com.jrobertgardzinski.util.Severity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Consumer;

class _PasswordResetEvaluator {

    private final List<Constraint<Email>> blockingConstraints;
    private final List<Constraint<Email>> warningConstraints;
    private final Consumer<List<ConstraintResult>> warningHandler;

    _PasswordResetEvaluator(
            List<Constraint<Email>> blockingConstraints,
            List<Constraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        this.blockingConstraints = blockingConstraints;
        this.warningConstraints = warningConstraints;
        this.warningHandler = warningHandler;
    }

    List<ConstraintResult> evaluateBlocking(Email email) {
        try (var scope = StructuredTaskScope.open()) {
            var subtasks = blockingConstraints.stream()
                    .map(c -> scope.fork(() ->
                            c.isSatisfied(email) ? null : new ConstraintResult(Severity.BLOCKING, c.code())))
                    .toList();
            scope.join();
            return subtasks.stream()
                    .filter(s -> s.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(StructuredTaskScope.Subtask::get)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while evaluating blocking constraints", e);
        }
    }

    void fireWarningsAsync(Email email) {
        if (!warningConstraints.isEmpty() && warningHandler != null) {
            Thread.startVirtualThread(() -> evaluateWarnings(email));
        }
    }

    private void evaluateWarnings(Email email) {
        try (var scope = StructuredTaskScope.open()) {
            var subtasks = warningConstraints.stream()
                    .map(c -> scope.fork(() ->
                            c.isSatisfied(email) ? null : new ConstraintResult(Severity.WARNING, c.code())))
                    .toList();
            scope.join();
            var violations = subtasks.stream()
                    .filter(s -> s.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(StructuredTaskScope.Subtask::get)
                    .filter(Objects::nonNull)
                    .toList();
            if (!violations.isEmpty()) {
                warningHandler.accept(violations);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
