package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

public class ToThrowingTest {

    @Test
    void toThrowingWhenSuccess() {
        assertThat(aSuccess(VALUE).toThrowing(_ -> THROWING_CAUSE)).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure() {
        assertThat(aFailure(VALUE).toThrowing(_ -> THROWING_CAUSE)).hasFailureCause(THROWING_CAUSE);
    }
}
