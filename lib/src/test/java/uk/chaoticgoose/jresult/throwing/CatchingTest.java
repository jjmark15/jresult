package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;

@NullMarked
public class CatchingTest {
    private static final Integer VALUE = 1;
    private static final AnException CAUSE = new AnException();

    @Test
    void handlesNonThrowingAction() {
        assertThat(Result.catching(AnException.class, this::nonThrowingMethod)).hasSuccessValue(VALUE);
    }

    @Test
    void catchesThrowingAction() {
        assertThat(Result.catching(AnException.class, this::throwingMethod)).hasFailureCause(CAUSE);
    }

    @Test
    void doesNotCatchExceptionsOfDifferentType() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            ThrowingResult<Integer, AnException> _ = Result.catching(AnException.class, this::runtimeThrowingMethod);
        });
    }

    @Test
    void catchesBaseCause() {
        assertThat(Result.catching(this::throwingMethod)).hasFailureCause(CAUSE);
    }

    private <T> T throwingMethod() throws AnException {
        throw CAUSE;
    }

    private <T> T runtimeThrowingMethod() throws RuntimeException {
        throw new RuntimeException();
    }

    private Integer nonThrowingMethod() {
        return VALUE;
    }
}
