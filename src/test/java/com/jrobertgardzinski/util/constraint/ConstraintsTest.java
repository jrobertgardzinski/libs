package com.jrobertgardzinski.util.constraint;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The kernel's own coverage. Until 2026-10-03 this module had no {@code src/test} at all, so the
 * four {@link Outcome} variants were exercised only through the products that import it — and
 * {@link Outcome.RejectedDueToInvariantBreakage}, the variant produced here and nowhere else, was
 * named by no test in the estate. These tests name it.
 */
@Epic("Constraint")
@Feature("Constraints")
class ConstraintsTest {

    private static final Supplier<String> CANDIDATE = () -> "candidate";

    private static ErrorConstraint<String> error(String code, boolean satisfied) {
        return new ErrorConstraint<>() {
            @Override
            public String code() {
                return code;
            }

            @Override
            public boolean isSatisfied(String value) {
                return satisfied;
            }
        };
    }

    private static WarningConstraint<String> warning(String code, boolean satisfied) {
        return new WarningConstraint<>() {
            @Override
            public String code() {
                return code;
            }

            @Override
            public boolean isSatisfied(String value) {
                return satisfied;
            }
        };
    }

    @Nested
    @Story("Judging a candidate")
    class Judging {

        @Test
        @DisplayName("allows a candidate no constraint objects to")
        void allowsWhenEverySatisfied() {
            Outcome<String> outcome = new Constraints<>(List.of(error("E1", true))).validate(CANDIDATE);

            assertThat(outcome).isInstanceOf(Outcome.Allowed.class);
            assertThat(outcome.findValue()).contains("candidate");
            assertThat(outcome.errorCodes()).isEmpty();
        }

        @Test
        @DisplayName("rejects with every failing error code, in declaration order")
        void rejectsWithAllFailingCodes() {
            Outcome<String> outcome = new Constraints<>(List.of(
                    error("E1", false),
                    error("E2", true),
                    error("E3", false))).validate(CANDIDATE);

            assertThat(outcome).isInstanceOf(Outcome.Rejected.class);
            assertThat(outcome.errorCodes()).containsExactly("E1", "E3");
            assertThat(outcome.findValue()).isEmpty();
        }

        @Test
        @DisplayName("a failing warning allows the candidate and reports the code")
        void warningDoesNotReject() {
            Outcome<String> outcome = new Constraints<>(
                    List.of(error("E1", true)),
                    List.of(warning("W1", false))).validate(CANDIDATE);

            assertThat(outcome).isInstanceOf(Outcome.AllowedWithWarning.class);
            assertThat(outcome.findValue()).contains("candidate");
            assertThat(outcome.errorCodes()).isEmpty();
            assertThat(((Outcome.AllowedWithWarning<String>) outcome).warningCodes()).containsExactly("W1");
        }

        @Test
        @DisplayName("an error outranks a warning: the warnings are never even asked")
        void errorWinsOverWarning() {
            Outcome<String> outcome = new Constraints<>(
                    List.of(error("E1", false)),
                    List.of(warning("W1", false))).validate(CANDIDATE);

            assertThat(outcome).isInstanceOf(Outcome.Rejected.class);
            assertThat(outcome.errorCodes()).containsExactly("E1");
        }
    }

    @Nested
    @Story("RejectedDueToInvariantBreakage")
    class InvariantBreakage {

        private final Constraints<String> constraints = new Constraints<>(List.of(error("E1", false)));

        @Test
        @DisplayName("a throwing supplier yields RejectedDueToInvariantBreakage, not Rejected")
        void throwingSupplierBreaksTheInvariant() {
            Outcome<String> outcome = constraints.validate(() -> {
                throw new IllegalArgumentException("MALFORMED");
            });

            assertThat(outcome).isInstanceOf(Outcome.RejectedDueToInvariantBreakage.class);
            assertThat(outcome.errorCodes()).containsExactly("MALFORMED");
            assertThat(outcome.findValue()).isEmpty();
        }

        @Test
        @DisplayName("the constraints are never consulted when the candidate cannot be built")
        void constraintsAreNotConsulted() {
            Outcome<String> outcome = constraints.validate(() -> {
                throw new IllegalStateException("BOOM");
            });

            assertThat(outcome.errorCodes()).doesNotContain("E1");
        }

        @Test
        @DisplayName("a message-less exception is described by its type, never as null")
        void messagelessExceptionIsNamedByItsType() {
            Outcome<String> outcome = constraints.validate(() -> {
                throw new NullPointerException();
            });

            assertThat(outcome).isInstanceOf(Outcome.RejectedDueToInvariantBreakage.class);
            assertThat(outcome.errorCodes())
                    .doesNotContainNull()
                    .containsExactly("NullPointerException");
        }

        @Test
        @DisplayName("a blank message is described by its type too")
        void blankMessageIsNamedByItsType() {
            Outcome<String> outcome = constraints.validate(() -> {
                throw new IllegalArgumentException("   ");
            });

            assertThat(outcome.errorCodes()).containsExactly("IllegalArgumentException");
        }

        @Test
        @DisplayName("an Error is not a refusal: it leaves the machinery untouched")
        void errorIsNotCaught() {
            assertThatThrownBy(() -> constraints.validate(() -> {
                throw new StackOverflowError();
            })).isInstanceOf(StackOverflowError.class);
        }

        @Test
        @DisplayName("a failing collaborator is indistinguishable from a refused value — documented, not fixed here")
        void aFailingCollaboratorLooksLikeARefusal() {
            Supplier<String> storeThatIsDown = () -> {
                throw new RuntimeException("connection refused");
            };

            Outcome<String> outcome = constraints.validate(storeThatIsDown);

            // The kernel knows no product's exception types, so an outage arrives as the same
            // variant a malformed value would. A caller that needs the two told apart maps its own
            // exception before handing the built candidate over; see validate's javadoc.
            assertThat(outcome).isInstanceOf(Outcome.RejectedDueToInvariantBreakage.class);
            assertThat(outcome.errorCodes()).containsExactly("connection refused");
        }
    }

    @Nested
    @Story("Mapping an outcome")
    class Mapping {

        @Test
        @DisplayName("map carries the value through Allowed")
        void mapsAllowed() {
            Outcome<Integer> mapped = new Constraints<>(List.<ErrorConstraint<String>>of())
                    .validate(CANDIDATE)
                    .map(String::length);

            assertThat(mapped.findValue()).contains("candidate".length());
        }

        @Test
        @DisplayName("map keeps the codes and never calls the mapper on a breakage")
        void mapsInvariantBreakageWithoutCallingTheMapper() {
            Outcome<Integer> mapped = new Constraints<>(List.<ErrorConstraint<String>>of())
                    .validate(() -> {
                        throw new IllegalArgumentException("MALFORMED");
                    })
                    .map(value -> {
                        throw new AssertionError("the mapper must not run");
                    });

            assertThat(mapped).isInstanceOf(Outcome.RejectedDueToInvariantBreakage.class);
            assertThat(mapped.errorCodes()).containsExactly("MALFORMED");
        }
    }
}
