package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.aFailure;
import static uk.chaoticgoose.jresult.ResultHelpers.aSuccess;

public class ToThrowingTest {
    private static final String VALUE = "value";
    private static final AnException CAUSE = new AnException();

    @Test
    void toThrowingWhenSuccess() {
        assertThat(aSuccess(VALUE).toThrowing(_ -> CAUSE)).hasSuccessValue(VALUE);
    }

    @Test
    void toThrowingWhenFailure() {
        assertThat(aFailure(VALUE).toThrowing(_ -> CAUSE)).hasFailureCause(CAUSE);
    }
}
