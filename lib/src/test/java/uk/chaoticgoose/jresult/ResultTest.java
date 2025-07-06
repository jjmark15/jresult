package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@NullMarked
class ResultTest {
    private static final AnException EXCEPTION = new AnException();
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
            ThrowingResult<Integer, AnException> _ = Result.catching(AnException.class, () -> {
                throw new AnotherException();
            });
        });
    }

    @Test
    void catchesBaseCause() {
        ResultAssert.assertThat(Result.catching(this::throwingMethod)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsSuccesses() {
        ResultAssert.assertThat(Result.catching(AnException.class, this::nonThrowingMethod).map(it -> it + 1)).hasSuccessValue(2);
    }

    @Test
    void mapsFailures() {
        ResultAssert.assertThat(Result.catching(AnException.class, this::<Integer>throwingMethod).map(it -> it + 1)).hasFailureCause(EXCEPTION);
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

    private <T> T throwingMethod() throws AnException {
        throw EXCEPTION;
    }

    private Integer nonThrowingMethod() {
        return VALUE;
    }

    private static class AnException extends Exception {}

    private static class AnotherException extends RuntimeException {}
}