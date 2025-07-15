package uk.chaoticgoose.jresult.throwing;

import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

public class ToNonThrowingTest {

    @Test
    void toThrowingWhenSuccess() {
        assertThat(aThrowingSuccess(VALUE).toNonThrowing(_ -> CAUSE)).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure() {
        assertThat(aThrowingFailure(THROWING_CAUSE).toNonThrowing(_ -> ANOTHER_CAUSE)).hasFailureCause(ANOTHER_CAUSE);
    }

    @Test
    void toThrowingWhenSuccess_withoutCauseMapping() {
        assertThat(aThrowingSuccess(VALUE).toNonThrowing()).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure_withoutCauseMapping() {
        assertThat(aThrowingFailure(THROWING_CAUSE).toNonThrowing()).hasFailureCause(THROWING_CAUSE);
    }
}
