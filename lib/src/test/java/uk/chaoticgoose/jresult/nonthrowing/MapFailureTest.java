package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnotherFailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.aFailure;
import static uk.chaoticgoose.jresult.ResultHelpers.aSuccess;

@NullMarked
public class MapFailureTest {
    private static final Integer VALUE = 1;
    private static final FailureCause CAUSE = new FailureCause(1);
    private static final AnotherFailureCause MAPPED_CAUSE = new AnotherFailureCause(2);

    @Test
    void successReturnsValue() {
        assertThat(aSuccess(VALUE).mapFailure(this::mapCause)).hasSuccessValue(VALUE);
    }

    @Test
    void mappedFailureReturnsMappedCause() {
        assertThat(aFailure(CAUSE).mapFailure(this::mapCause)).hasFailureCause(MAPPED_CAUSE);
    }

    private AnotherFailureCause mapCause(FailureCause ignore) {
        return MAPPED_CAUSE;
    }
}
