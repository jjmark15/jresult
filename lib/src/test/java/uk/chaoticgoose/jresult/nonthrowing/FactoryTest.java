package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCauses;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;

public class FactoryTest {
    private static final String VALUE = "value";
    private static final FailureCauses CAUSE = new FailureCause(1);

    @Test
    void success() {
        assertThat(Result.success(VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void failure() {
        assertThat(Result.failure(CAUSE)).hasFailureCause(CAUSE);
    }

    @Test
    @SuppressWarnings("all")
    void successValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> Result.success(null));
    }

    @Test
    @SuppressWarnings("all")
    void failureValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> Result.failure(null));
    }
}
