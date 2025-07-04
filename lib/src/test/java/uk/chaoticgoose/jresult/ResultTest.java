package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@NullMarked
class ResultTest {
    private static final Exception EXCEPTION = new Exception();
    private static final int VALUE = 1;

    @Test
    void handlesNonThrowingAction() {
        Result<Integer, Exception> result = Result.catching(this::nonThrowingMethod);
        assertThat(result.value()).hasValue(VALUE);
        assertThat(result.throwable()).isEmpty();
    }

    @Test
    void catchesThrowingAction() {
        Result<Integer, Exception> result = Result.catching(this::throwingMethod);
        assertThat(result.value()).isEmpty();
        assertThat(result.throwable()).hasValue(EXCEPTION);
    }

    @Test
    void throwsCaughtException() {
        Result<Integer, Exception> result = Result.catching(this::throwingMethod);
        assertThatExceptionOfType(Exception.class).isThrownBy(result::valueOrThrow);
    }

    @Test
    void returnsValue() throws Exception {
        Result<Integer, Exception> result = Result.catching(this::nonThrowingMethod);
        assertThat(result.valueOrThrow()).isEqualTo(VALUE);
    }

    private <T> T throwingMethod() throws Exception {
        throw EXCEPTION;
    }

    private Integer nonThrowingMethod() {
        return VALUE;
    }
}