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
        Result<Integer, AnException> result = Result.catching(AnException.class, this::nonThrowingMethod);
        assertThat(result.value()).hasValue(VALUE);
        assertThat(result.exception()).isEmpty();
    }

    @Test
    void catchesThrowingAction() {
        Result<Integer, AnException> result = Result.catching(AnException.class, this::throwingMethod);
        assertThat(result.value()).isEmpty();
        assertThat(result.exception()).hasValue(EXCEPTION);
    }

    @Test
    void throwsCaughtException() {
        Result<Integer, AnException> result = Result.catching(AnException.class, this::throwingMethod);
        assertThatExceptionOfType(Exception.class).isThrownBy(result::valueOrThrow);
    }

    @Test
    void returnsValue() throws Exception {
        Result<Integer, AnException> result = Result.catching(AnException.class, this::nonThrowingMethod);
        assertThat(result.valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void doesNotCatchExceptionsOfDifferentType() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            Result<Integer, AnException> _ = Result.catching(AnException.class, () -> {
                throw new AnotherException();
            });
        });
    }

    @Test
    void catchesBaseException() {
        Result<Integer, Exception> result = Result.catching(this::throwingMethod);
        assertThat(result.value()).isEmpty();
        assertThat(result.exception()).hasValue(EXCEPTION);
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