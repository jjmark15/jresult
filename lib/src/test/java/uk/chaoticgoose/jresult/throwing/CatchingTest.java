package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.ASuccessValue;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.THROWING_CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

@NullMarked
public class CatchingTest {

    @Test
    void handlesNonThrowingAction() {
        assertThat(Result.catching(AnException.class, this::nonThrowingMethod)).hasSuccessValue(VALUE);
    }

    @Test
    void catchesThrowingAction() {
        assertThat(Result.catching(AnException.class, this::throwingMethod)).hasFailureCause(THROWING_CAUSE);
    }

    @Test
    void doesNotCatchExceptionsOfDifferentType() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            ThrowingResult<Integer, AnException> _ = Result.catching(AnException.class, this::runtimeThrowingMethod);
        });
    }

    @Test
    void catchesBaseCause() {
        assertThat(Result.catching(this::throwingMethod)).hasFailureCause(THROWING_CAUSE);
    }

    private <T> T throwingMethod() throws AnException {
        throw THROWING_CAUSE;
    }

    private <T> T runtimeThrowingMethod() throws RuntimeException {
        throw new RuntimeException();
    }

    private ASuccessValue nonThrowingMethod() {
        return VALUE;
    }
}
