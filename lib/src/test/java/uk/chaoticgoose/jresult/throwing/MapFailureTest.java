package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapFailureTest {

    @Test
    void successReturnsValue() {
        assertThat(aThrowingSuccess(VALUE).mapFailure(this::mapCause)).hasSuccessValue(VALUE);
    }

    @Test
    void mappedFailureReturnsMappedCause() {
        assertThat(aThrowingFailure(THROWING_CAUSE).mapFailure(this::mapCause)).hasFailureCause(ANOTHER_THROWING_CAUSE);
    }

    private AnotherException mapCause(AnException ignore) {
        return ANOTHER_THROWING_CAUSE;
    }
}
