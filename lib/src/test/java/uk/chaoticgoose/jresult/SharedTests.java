package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ResultHelpers.AFailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class SharedTests<BC> {

    abstract <T, C extends BC> BaseResult<T, ? extends C> success(T value);

    abstract <T, C extends BC> BaseResult<T, ? extends C> failure(C cause);

    abstract <C extends BC> C aCause();

    @Nested
    static class NonThrowingTest extends SharedTests<FailureCause> {
        @Override
        <T, C extends FailureCause> BaseResult<T, C> success(T value) {
            return Result.success(value);
        }

        @Override
        <T, C extends FailureCause> BaseResult<T, C> failure(C cause) {
            return Result.failure(cause);
        }

        @Override
        @SuppressWarnings("unchecked")
        AFailureCause aCause() {
            return new AFailureCause(1);
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

    @Nested
    class ValueTest {
        @Test
        void successReturnsPresentValue() {
            assertThat(success(VALUE).value()).hasValue(VALUE);
        }

        @Test
        void failureReturnsEmptyValue() {
            assertThat(failure(aCause()).value()).isEmpty();
        }
    }

    @Nested
    class ValueOrNullTest {
        @Test
        void successReturnsValue() {
            assertThat(success(VALUE).valueOrNull()).isEqualTo(VALUE);
        }

        @Test
        void failureReturnsNull() {
            assertThat(failure(aCause()).valueOrNull()).isNull();
        }
    }

    @Nested
    class CauseTest {
        @Test
        void failureReturnsCause() {
            assertThat(failure(aCause()).cause()).contains(aCause());
        }

        @Test
        void successReturnsEmpty() {
            assertThat(success(VALUE).cause()).isEmpty();
        }
    }

    @Nested
    class CauseOrNullTest {
        @Test
        void failureReturnsCause() {
            assertThat(failure(aCause()).causeOrNull()).isEqualTo(aCause());
        }

        @Test
        void successReturnsNull() {
            assertThat(success(VALUE).causeOrNull()).isNull();
        }
    }

    @Nested
    class OrElseTest {
        @Test
        void successReturnsValue() {
            assertThat(success(VALUE).orElse(ANOTHER_VALUE)).isEqualTo(VALUE);
        }

        @Test
        void failureReturnsOtherValue() {
            assertThat(failure(aCause()).orElse(ANOTHER_VALUE)).isEqualTo(ANOTHER_VALUE);
        }
    }

    @Nested
    class OrElseGetTest {
        @Test
        void successReturnsValue() {
            assertThat(success(VALUE).orElseGet(() -> ANOTHER_VALUE)).isEqualTo(VALUE);
        }

        @Test
        void failureReturnsOtherValue() {
            assertThat(failure(aCause()).orElseGet(() -> ANOTHER_VALUE)).isEqualTo(ANOTHER_VALUE);
        }
    }

    @Nested
    class IsSuccessTest {
        @Test
        void successReturnsTrue() {
            assertThat(success(VALUE).isSuccess()).isTrue();
        }

        @Test
        void failureReturnsFalse() {
            assertThat(failure(aCause()).isSuccess()).isFalse();
        }
    }

    @Nested
    class IsFailureTest {
        @Test
        void successReturnsTrue() {
            assertThat(success(VALUE).isFailure()).isFalse();
        }

        @Test
        void failureReturnsFalse() {
            assertThat(failure(aCause()).isFailure()).isTrue() ;
        }
    }
}
