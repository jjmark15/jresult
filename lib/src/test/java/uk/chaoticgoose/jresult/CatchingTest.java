package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.TestTypes.*;

public class CatchingTest {
    private static final TestValue VALUE = aValue();
    private static final TestException EXCEPTION = anException();
    private static final TestRuntimeException RUNTIME_EXCEPTION = aRuntimeException();

    @Nested
    class WithClassArgumentTest {

        @Test
        void passesNonThrowingOperationAsSuccess() {
            assertThat(Result.catching(TestException.class, () -> VALUE)).hasSuccessValue(VALUE);
        }

        @Test
        void catchesThrowingOperationAsFailure() {
            ThrowingSupplier<TestValue, TestException> mapper = throwingSupplier(EXCEPTION);

            assertThat(Result.catching(TestException.class, mapper)).hasFailureCause(EXCEPTION);
        }

        @Test
        void catchesThrowingOperationAsFailure_throwingSubtype() {
            ThrowingSupplier<TestValue, TestException> mapper = throwingSupplier(EXCEPTION);

            assertThat(Result.catching(Exception.class, mapper)).hasFailureCause(EXCEPTION);
        }

        @Test
        void passesNonThrowingOperationAsSuccess_returningSubtype() {
            ThrowingSupplier<TestValue, TestException> mapper = () -> VALUE;

            Result<Object, TestException> result = Result.catching(TestException.class, mapper);

            assertThat(result).hasSuccessValue(VALUE);
        }

        @Test
        void doesNotCatchOtherRuntimeExceptionTypes() {
            ThrowingSupplier<TestValue, TestException> mapper = () -> {
                throw RUNTIME_EXCEPTION;
            };

            assertThatExceptionOfType(TestRuntimeException.class)
                .isThrownBy(() -> Result.catching(TestException.class, mapper));
        }

        @Test
        void catchesRuntimeExceptions() {
            ThrowingSupplier<TestValue, TestRuntimeException> mapper = throwingSupplier(RUNTIME_EXCEPTION);

            assertThat(Result.catching(TestRuntimeException.class, mapper)).hasFailureCause(RUNTIME_EXCEPTION);
        }
    }

    @Nested
    class WithoutClassArgumentTest {

        @Test
        void passesNonThrowingOperationAsSuccess() {
            assertThat(Result.catching(() -> VALUE)).hasSuccessValue(VALUE);
        }

        @Test
        void catchesThrowingOperationAsFailure() {
            ThrowingSupplier<TestValue, TestException> mapper = throwingSupplier(EXCEPTION);

            assertThat(Result.catching(mapper)).hasFailureCause(EXCEPTION);
        }

        @Test
        void catchesThrowingOperationAsFailure_throwingSubtype() {
            ThrowingSupplier<TestValue, TestException> mapper = throwingSupplier(EXCEPTION);

            Result<TestValue, Exception> result = Result.catching(mapper);

            assertThat(result).hasFailureCause(EXCEPTION);
        }

        @Test
        void passesNonThrowingOperationAsSuccess_returningSubtype() {
            ThrowingSupplier<TestValue, TestException> mapper = () -> VALUE;

            Result<Object, Exception> result = Result.catching(mapper);

            assertThat(result).hasSuccessValue(VALUE);
        }

        @Test
        void catchesRuntimeExceptions() {
            ThrowingSupplier<TestValue, TestException> mapper = () -> {
                throw RUNTIME_EXCEPTION;
            };

            assertThat(Result.catching(mapper)).hasFailureCause(RUNTIME_EXCEPTION);
        }
    }

    private <T, E extends Exception> ThrowingSupplier<T, E> throwingSupplier(E exception) {
        return () -> {
            throw exception;
        };
    }
}
