package uk.chaoticgoose.jresult.throwing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ThrowingResult;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.THROWING_CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

public class FactoryTest {

    @Test
    void success() {
        assertThat(ThrowingResult.success(VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void failure() {
        assertThat(ThrowingResult.failure(THROWING_CAUSE)).hasFailureCause(THROWING_CAUSE);
    }

    @Test
    @SuppressWarnings("all")
    void successValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> ThrowingResult.success(null));
    }

    @Test
    @SuppressWarnings("all")
    void failureValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> ThrowingResult.failure(null));
    }
}
