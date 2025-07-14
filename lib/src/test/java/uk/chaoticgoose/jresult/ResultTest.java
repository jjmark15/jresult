package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ResultHelpers.AnotherException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
class ResultTest {
    private static final AnException EXCEPTION = new AnException();
    private static final AnotherException ANOTHER_EXCEPTION = new AnotherException();
    private static final int VALUE = 1;

    @Test
    void handlesNonThrowingAction() {
        ResultAssert.assertThat(Result.catching(AnException.class, this::nonThrowingMethod)).hasSuccessValue(VALUE);
    }

    @Test
    void catchesThrowingAction() {
        ResultAssert.assertThat(Result.catching(AnException.class, this::throwingMethod)).hasFailureCause(EXCEPTION);
    }

    @Test
    void throwingFailureThrowsCaughtCause() {
        assertThatExceptionOfType(AnException.class).isThrownBy(() -> aThrowingFailure(EXCEPTION).valueOrThrow());
    }

    @Test
    void throwingSuccessReturnsValue() throws Exception {
        assertThat(aThrowingSuccess(VALUE).valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void nonThrowingFailureThrowsNoSuchElementException() {
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> aFailure(1).valueOrThrow())
            .withMessage("Result is a failure");
    }

    @Test
    void nonThrowingSuccessReturnsValue() {
        assertThat(aSuccess(VALUE).valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void doesNotCatchExceptionsOfDifferentType() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            ThrowingResult<Integer, AnException> _ = Result.catching(AnException.class, this::anotherThrowingMethod);
        });
    }

    @Test
    void catchesBaseCause() {
        ResultAssert.assertThat(Result.catching(this::throwingMethod)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsValueWhenSuccess() {
        ResultAssert.assertThat(aSuccess(1).map(it -> it + 1)).hasSuccessValue(2);
    }

    @Test
    void mapsValueWhenFailure() {
        ResultAssert.assertThat(aThrowingFailure(EXCEPTION).map(it -> it + 1)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsCauseWhenSuccess() {
        ResultAssert.assertThat(aSuccess(1).mapFailure(AnotherFailureCause::from)).hasSuccessValue(1);
    }

    @Test
    void mapsCauseWhenFailure() {
        var mapped = new RuntimeException(EXCEPTION);
        ResultAssert.assertThat(aThrowingFailure(EXCEPTION).mapFailure((_) -> mapped)).hasFailureCause(mapped);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenInitialFailureWithoutExceptionTyping() {
        ResultAssert.assertThat(aThrowingFailure(EXCEPTION)
                    .mapCatching(_ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenInitialFailure() {
        ResultAssert.assertThat(aThrowingFailure(EXCEPTION)
                    .mapCatching(AnotherException.class, _ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSecondaryFailureWithoutExceptionTyping() {
        ResultAssert.assertThat(aThrowingSuccess(VALUE)
                    .mapCatching(_ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(ANOTHER_EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSecondaryFailure() {
        ResultAssert.assertThat(aThrowingSuccess(1)
                    .mapCatching(AnotherException.class, _ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(ANOTHER_EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSuccess() {
        ResultAssert.assertThat(aThrowingSuccess(1)
                    .mapCatching(AnotherException.class, _ -> 2, cause -> cause.map(c -> c, c -> c)))
            .hasSuccessValue(2);
    }

    @Test
    void toThrowingWhenSuccess() {
        ResultAssert.assertThat(aSuccess(1).toThrowing(_ -> EXCEPTION)).hasSuccessValue(1);
    }

    @Test
    void toThrowingWhenFailure() {
        ResultAssert.assertThat(aFailure(1).toThrowing(_ -> EXCEPTION)).hasFailureCause(EXCEPTION);
    }

    @Test
    void toNonThrowingWhenSuccess() {
        ResultAssert.assertThat(Result.throwingSuccess(1).toNonThrowing()).hasSuccessValue(1);
    }

    @Test
    void toNonThrowingWhenSuccessWithCauseMapping() {
        ResultAssert.assertThat(Result.throwingSuccess(1).toNonThrowing(_ -> 2)).hasSuccessValue(1);
    }

    @Test
    void toNonThrowingWhenFailure() {
        ResultAssert.assertThat(Result.throwingFailure(EXCEPTION).toNonThrowing(_ -> 1)).hasFailureCause(1);
    }

    private <T> T throwingMethod() throws AnException {
        throw EXCEPTION;
    }

    private <T> T anotherThrowingMethod() throws AnotherException {
        throw ANOTHER_EXCEPTION;
    }

    private Integer nonThrowingMethod() {
        return VALUE;
    }
}