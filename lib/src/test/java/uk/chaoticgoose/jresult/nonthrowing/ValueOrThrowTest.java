package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCauses;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ValueOrThrowTest {
    private static final String VALUE = "value";
    private static final FailureCauses CAUSE = new FailureCause(1);

    @Test
    void successReturnsValue() {
        assertThat(Result.success(VALUE).valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void failureThrows() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Result.failure(CAUSE).valueOrThrow()).withMessage("Result is a failure");
    }
}
