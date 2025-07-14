package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCauses;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class SharedTests<BC> {

    abstract <T, C extends BC> BaseResult<T, ? extends C> success(T value);

    abstract <T, C extends BC> BaseResult<T, ? extends C> failure(C cause);

    abstract <C extends BC> C aCause();

    @Nested
    static class NonThrowingTest extends SharedTests<FailureCauses> {
        @Override
        <T, C extends FailureCauses> BaseResult<T, C> success(T value) {
            return Result.success(value);
        }

        @Override
        <T, C extends FailureCauses> BaseResult<T, C> failure(C cause) {
            return Result.failure(cause);
        }

        @Override
        @SuppressWarnings("unchecked")
        FailureCause aCause() {
            return new FailureCause(1);
        }
    }

    @Nested
    static class ThrowingTest extends SharedTests<Exception> {
        private static final AnException EXCEPTION = new AnException();

        @Override
        <T, C extends Exception> BaseResult<T, C> success(T value) {
            return Result.success(value);
        }

        @Override
        <T, C extends Exception> BaseResult<T, C> failure(C cause) {
            return Result.failure(cause);
        }

        @Override
        @SuppressWarnings("unchecked")
        AnException aCause() {
            return EXCEPTION;
        }
    }

    private static final Integer VALUE = 1;
    private static final Integer ANOTHER_VALUE = 2;

    @Test
    void value_successReturnsPresentValue() {
        assertThat(success(VALUE).value()).hasValue(VALUE);
    }

    @Test
    void value_failureReturnsEmptyValue() {
        assertThat(failure(aCause()).value()).isEmpty();
    }

    @Test
    void valueOrNull_successReturnsValue() {
        assertThat(success(VALUE).valueOrNull()).isEqualTo(VALUE);
    }

    @Test
    void valueOrNull_failureReturnsNull() {
        assertThat(failure(aCause()).valueOrNull()).isNull();
    }

    @Test
    void cause_failureReturnsCause() {
        assertThat(failure(aCause()).cause()).contains(aCause());
    }

    @Test
    void cause_successReturnsEmpty() {
        assertThat(success(VALUE).cause()).isEmpty();
    }

    @Test
    void causeOrNull_failureReturnsCause() {
        assertThat(failure(aCause()).causeOrNull()).isEqualTo(aCause());
    }

    @Test
    void causeOrNull_successReturnsNull() {
        assertThat(success(VALUE).causeOrNull()).isNull();
    }

    @Test
    void orElse_successReturnsValue() {
        assertThat(success(VALUE).orElse(ANOTHER_VALUE)).isEqualTo(VALUE);
    }

    @Test
    void orElse_failureReturnsOtherValue() {
        assertThat(failure(aCause()).orElse(ANOTHER_VALUE)).isEqualTo(ANOTHER_VALUE);
    }

    @Test
    void orElseGet_successReturnsValue() {
        assertThat(success(VALUE).orElseGet(() -> ANOTHER_VALUE)).isEqualTo(VALUE);
    }

    @Test
    void orElseGet_failureReturnsOtherValue() {
        assertThat(failure(aCause()).orElseGet(() -> ANOTHER_VALUE)).isEqualTo(ANOTHER_VALUE);
    }

    @Test
    void isSuccess_successReturnsTrue() {
        assertThat(success(VALUE).isSuccess()).isTrue();
    }

    @Test
    void isSuccess_failureReturnsFalse() {
        assertThat(failure(aCause()).isSuccess()).isFalse();
    }

    @Test
    void isFailure_successReturnsTrue() {
        assertThat(success(VALUE).isFailure()).isFalse();
    }

    @Test
    void isFailure_failureReturnsFalse() {
        assertThat(failure(aCause()).isFailure()).isTrue() ;
    }
}
