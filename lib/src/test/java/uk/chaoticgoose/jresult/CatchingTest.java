package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultUtils.ThrowingSupplier;

import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultUtils.catching;
import static uk.chaoticgoose.jresult.TestTypes.*;

public class CatchingTest {
    private static final TestValue VALUE = aValue();
    private static final TestException EXCEPTION = anException();

    @Test
    void catching_passesNonThrowingOperationAsSuccess() {
        assertThat(catching(TestException.class, () -> VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void catching_catchesThrowingOperationAsFailure() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(catching(TestException.class, func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catching_catchesThrowingOperationAsFailure_withSupertype() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(catching(Exception.class, func)).hasFailureCause(EXCEPTION);
    }

    @Test
    void catching_doesNotCatchOtherExceptionTypes() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw new RuntimeException();
        };

        assertThatRuntimeException().isThrownBy(() -> catching(TestException.class, func));
    }

    @Test
    void catchingBase_passesNonThrowingOperationAsSuccess() {
        assertThat(catching(() -> VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void catchingBase_catchesThrowingOperationAsFailure() {
        ThrowingSupplier<TestValue, TestException> func = () -> {
            throw EXCEPTION;
        };

        assertThat(catching(func)).hasFailureCause(EXCEPTION);
    }
}
