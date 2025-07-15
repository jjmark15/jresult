package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

public class FactoryTest {

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
