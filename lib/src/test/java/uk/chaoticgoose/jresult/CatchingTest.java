package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.TestTypes.*;

public class CatchingTest {
    private static final TestValue VALUE = aValue();
    private static final TestException EXCEPTION = anException();

    @Test
    void catching_passesNonThrowingOperationAsSuccess() {
        assertThat(Result.catching(TestException.class, () -> VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void catching_catchesThrowingOperationAsFailure() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(Result.catching(TestException.class, func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catching_catchesThrowingOperationAsFailure_withSupertype() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(Result.catching(Exception.class, func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catching_passesNonThrowingOperationAsSuccess_withSupertype() {
        ThrowingSupplier<TestValue, TestException> func = () -> VALUE;

        assertThat(Result.<Object, TestException>catching(TestException.class, func)).hasSuccessValue(VALUE);
    }

    @Test
    void catching_doesNotCatchOtherExceptionTypes() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw new RuntimeException();
        };

        assertThatRuntimeException().isThrownBy(() -> Result.catching(TestException.class, func));
    }

    @Test
    void catchingBase_passesNonThrowingOperationAsSuccess() {
        assertThat(Result.catching(() -> VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void catchingBase_catchesThrowingOperationAsFailure() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(Result.catching(func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catchingBase_catchesThrowingOperationAsFailure_withSupertype() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(Result.catching(func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catchingBase_passesNonThrowingOperationAsSuccess_withSupertype() {
        ThrowingSupplier<TestValue, TestException> func = () -> VALUE;

        assertThat(Result.<Object>catching(func)).hasSuccessValue(VALUE);
    }
}
