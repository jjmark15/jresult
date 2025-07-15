package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnotherFailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.AFailureCause;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapFailureTest {

    @Test
    void successReturnsValue() {
        assertThat(aSuccess(VALUE).mapFailure(this::mapCause)).hasSuccessValue(VALUE);
    }

    @Test
    void mappedFailureReturnsMappedCause() {
        assertThat(aFailure(CAUSE).mapFailure(this::mapCause)).hasFailureCause(ANOTHER_CAUSE);
    }

    private AnotherFailureCause mapCause(AFailureCause ignore) {
        return ANOTHER_CAUSE;
    }
}
