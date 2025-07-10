package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
    void throwsCaughtCause() {
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> Result.catching(AnException.class, this::throwingMethod).valueOrThrow());
    }

    @Test
    void returnsValue() throws Exception {
        ThrowingResult<Integer, AnException> result = Result.catching(AnException.class, this::nonThrowingMethod);
        assertThat(result.valueOrThrow()).isEqualTo(VALUE);
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
        ResultAssert.assertThat(Result.throwingSuccess(1).map(it -> it + 1)).hasSuccessValue(2);
    }

    @Test
    void mapsValueWhenFailure() {
        ResultAssert.assertThat(Result.<Integer, AnException>throwingFailure(EXCEPTION).map(it -> it + 1)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsCauseWhenSuccess() {
        ResultAssert.assertThat(Result.throwingSuccess(1).mapFailure(RuntimeException::new)).hasSuccessValue(1);
    }

    @Test
    void mapsCauseWhenFailure() {
        var mapped = new RuntimeException(EXCEPTION);
        ResultAssert.assertThat(Result.<Integer, AnException>throwingFailure(EXCEPTION).mapFailure((_) -> mapped)).hasFailureCause(mapped);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenInitialFailureWithoutExceptionTyping() {
        ResultAssert.assertThat(ThrowingResult.failure(EXCEPTION)
                .mapCatching(_ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenInitialFailure() {
        ResultAssert.assertThat(ThrowingResult.failure(EXCEPTION)
                .mapCatching(AnotherException.class, _ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSecondaryFailureWithoutExceptionTyping() {
        ResultAssert.assertThat(ThrowingResult.success(1)
                .mapCatching(_ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(ANOTHER_EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSecondaryFailure() {
        ResultAssert.assertThat(ThrowingResult.success(1)
                .mapCatching(AnotherException.class, _ -> anotherThrowingMethod(), cause -> cause.map(c -> c, c -> c)))
            .hasFailureCause(ANOTHER_EXCEPTION);
    }

    @Test
    void combinesThrowingFailuresFromMappingWhenSuccess() {
        ResultAssert.assertThat(ThrowingResult.<Integer, AnException>success(1)
                .mapCatching(AnotherException.class, _ -> 2, cause -> cause.map(c -> c, c -> c)))
            .hasSuccessValue(2);
    }

    @Test
    void orElseReturnsValue() {
        assertThat(Result.success(1).orElse(2)).isEqualTo(1);
    }

    @Test
    void orElseReturnsValueWhenFailure() {
        assertThat(Result.failure("failure").orElse(1)).isEqualTo(1);
    }

    @Test
    void orElseGetReturnsValue() {
        assertThat(Result.success(1).orElseGet(() -> 2)).isEqualTo(1);
    }

    @Test
    void orElseGetReturnsValueWhenFailure() {
        assertThat(Result.failure("failure").orElseGet(() -> 1)).isEqualTo(1);
    }

    @Test
    void isSuccessWhenSuccess() {
        assertThat(Result.success(1).isSuccess()).isTrue();
    }

    @Test
    void isNotSuccessWhenFailure() {
        assertThat(Result.failure(1).isSuccess()).isFalse();
    }

    @Test
    void isFailureWhenFailure() {
        assertThat(Result.failure(1).isFailure()).isTrue();
    }

    @Test
    void isNotFailureWhenSuccess() {
        assertThat(Result.success(1).isFailure()).isFalse();
    }

    @Test
    void toThrowingWhenSuccess() {
        ResultAssert.assertThat(Result.success(1).toThrowing(_ -> EXCEPTION)).hasSuccessValue(1);
    }

    @Test
    void toThrowingWhenFailure() {
        ResultAssert.assertThat(Result.failure(1).toThrowing(_ -> EXCEPTION)).hasFailureCause(EXCEPTION);
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

    private sealed interface FailureCauses {}

    private static final class AnException extends Exception implements FailureCauses {}

    private static final class AnotherException extends RuntimeException implements FailureCauses {}
}