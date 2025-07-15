package uk.chaoticgoose.jresult.throwing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;

public class FactoryTest {
    private static final String VALUE = "value";
    private static final Exception CAUSE = new AnException();

    @Test
    void success() {
        assertThat(ThrowingResult.success(VALUE)).hasSuccessValue(VALUE);
    }

    @Test
    void failure() {
        assertThat(ThrowingResult.failure(CAUSE)).hasFailureCause(CAUSE);
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
