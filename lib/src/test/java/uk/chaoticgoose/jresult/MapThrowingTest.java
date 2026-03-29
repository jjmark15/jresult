package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

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

    @Test
    void mapsSuccessWithNonThrowingOperationAsSuccess() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> VALUE_2;

        assertThat(Result.mapThrowing(SUCCESS, TestException.class, func, this::neverCalled)).hasSuccessValue(VALUE_2);
    }

    @Test
    void mapsSuccessWithNonThrowingOperationAsSuccess_subtype() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> VALUE_2;
        Function<TestCause, TestException> failureMapper = this::neverCalled;

        Result<Object, Exception> result = Result.mapThrowing(SUCCESS, Exception.class, func, failureMapper);

        assertThat(result).hasSuccessValue(VALUE_2);
    }

    @Test
    void mapsFailureWithNonThrowingOperationAsSuccess() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = v -> VALUE_2;

        Function<TestCause, TestException> failureMapper = _ -> EXCEPTION;

        assertThat(Result.mapThrowing(FAILURE, TestException.class, func, failureMapper)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsSuccessWithThrowingOperationAsFailure() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> {
            throw EXCEPTION;
        };

        assertThat(Result.mapThrowing(SUCCESS, TestException.class, func, this::neverCalled)).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsSuccessWithThrowingOperationAsFailure_subtype() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> {
            throw EXCEPTION;
        };
        Function<TestCause, TestException> failureMapper = this::neverCalled;

        Result<Object, Exception> result = Result.mapThrowing(SUCCESS, Exception.class, func, failureMapper);

        assertThat(result).hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsFailureWithThrowingOperationAsFailure() {
        ThrowingFunction<TestValue, TestValue2, TestException> func = _ -> {
            throw EXCEPTION;
        };

        Function<TestCause, TestException> failureMapper = _ -> EXCEPTION;

        assertThat(Result.mapThrowing(FAILURE, TestException.class, func, failureMapper)).hasFailureCause(EXCEPTION);
    }

    private <T, U> U neverCalled(T in) {
        throw new IllegalStateException("should never be called");
    }
}
