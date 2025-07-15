package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapFailureTest {
    private static final Integer VALUE = 1;
    private static final AnException CAUSE = new AnException();
    private static final AnotherException MAPPED_CAUSE = new AnotherException();

    @Test
    void successReturnsValue() {
        assertThat(aThrowingSuccess(VALUE).mapFailure(this::mapCause)).hasSuccessValue(VALUE);
    }

    @Test
    void mappedFailureReturnsMappedCause() {
        assertThat(aThrowingFailure(CAUSE).mapFailure(this::mapCause)).hasFailureCause(MAPPED_CAUSE);
    }

    private AnotherException mapCause(AnException ignore) {
        return MAPPED_CAUSE;
    }
}
