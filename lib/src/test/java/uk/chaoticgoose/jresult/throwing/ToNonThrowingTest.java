package uk.chaoticgoose.jresult.throwing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ResultHelpers.AnotherException;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.aThrowingFailure;
import static uk.chaoticgoose.jresult.ResultHelpers.aThrowingSuccess;

public class ToNonThrowingTest {
    private static final String VALUE = "value";
    private static final AnException CAUSE = new AnException();
    private static final AnotherException OTHER_CAUSE = new AnotherException();

    @Test
    void toThrowingWhenSuccess() {
        assertThat(aThrowingSuccess(VALUE).toNonThrowing(_ -> CAUSE)).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure() {
        assertThat(aThrowingFailure(CAUSE).toNonThrowing(_ -> OTHER_CAUSE)).hasFailureCause(OTHER_CAUSE);
    }

    @Test
    void toThrowingWhenSuccess_withoutCauseMapping() {
        assertThat(aThrowingSuccess(VALUE).toNonThrowing()).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure_withoutCauseMapping() {
        assertThat(aThrowingFailure(CAUSE).toNonThrowing()).hasFailureCause(CAUSE);
    }
}
