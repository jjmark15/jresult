package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.Result.failure;
import static uk.chaoticgoose.jresult.Result.success;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.TestTypes.*;

public class MapThrowingTest {
    private static final TestValue VALUE = aValue();
    private static final TestValue2 VALUE_2 = new TestValue2(VALUE.value());
    private static final TestCause CAUSE = aCause();
    private static final Success<TestValue, TestCause> SUCCESS = success(VALUE);
    private static final Failure<TestValue, TestCause> FAILURE = failure(CAUSE);
    private static final TestException EXCEPTION = new TestException(CAUSE.value());
    private static final TestRuntimeException RUNTIME_EXCEPTION = aRuntimeException();

    @Test
    void mapsSuccessWithNonThrowingOperationAsSuccess() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> VALUE_2;

        assertThat(Result.mapThrowing(SUCCESS, TestException.class, func, this::neverCalled)).hasSuccessValue(VALUE_2);
    }

    @Test
    void mapsSuccessWithNonThrowingOperationAsSuccess_returningSubtype() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = _ -> VALUE_2;
        Function<TestCause, TestException> failureMapper = this::neverCalled;

        Result<Object, Exception> result = Result.mapThrowing(SUCCESS, Exception.class, mapper, failureMapper);

        assertThat(result).hasSuccessValue(VALUE_2);
    }

    @Test
    void mapsFailureWithNonThrowingOperationAsSuccess() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = this::neverCalled;
        Function<TestCause, TestException> failureMapper = _ -> EXCEPTION;

        assertThat(Result.mapThrowing(FAILURE, TestException.class, mapper, failureMapper)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsSuccessWithThrowingOperationAsFailure() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = mapThrowing(EXCEPTION);

        assertThat(Result.mapThrowing(SUCCESS, TestException.class, mapper, this::neverCalled))
            .hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsSuccessWithThrowingOperationAsFailure_throwingSubtype() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = mapThrowing(EXCEPTION);
        Function<TestCause, TestException> failureMapper = this::neverCalled;

        Result<Object, Exception> result = Result.mapThrowing(SUCCESS, Exception.class, mapper, failureMapper);

        assertThat(result).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsFailureWithThrowingOperationAsFailure() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = this::neverCalled;
        Function<TestCause, TestException> failureMapper = _ -> EXCEPTION;

        assertThat(Result.mapThrowing(FAILURE, TestException.class, mapper, failureMapper)).hasFailureCause(EXCEPTION);
    }

    @Test
    void doesNotCatchOtherRuntimeExceptionTypes() {
        ThrowingFunction<TestValue, TestValue2, TestException> mapper = _ -> {
            throw RUNTIME_EXCEPTION;
        };
        Function<TestCause, TestException> failureMapper = this::neverCalled;

        assertThatExceptionOfType(TestRuntimeException.class)
            .isThrownBy(() -> Result.mapThrowing(SUCCESS, TestException.class, mapper, failureMapper));
    }

    @Test
    void catchesRuntimeExceptions() {
        ThrowingFunction<TestValue, TestValue2, TestRuntimeException> mapper = _ -> {
            throw RUNTIME_EXCEPTION;
        };
        Function<TestCause, TestRuntimeException> failureMapper = this::neverCalled;

        assertThat(Result.mapThrowing(SUCCESS, TestRuntimeException.class, mapper, failureMapper))
            .hasFailureCause(RUNTIME_EXCEPTION);
    }

    private <T, U> U neverCalled(T in) {
        throw new IllegalStateException("should never be called");
    }

    private <T, U, E extends Exception> ThrowingFunction<T, U, E> mapThrowing(E exception) {
        return _ -> {
            throw exception;
        };
    }
}
