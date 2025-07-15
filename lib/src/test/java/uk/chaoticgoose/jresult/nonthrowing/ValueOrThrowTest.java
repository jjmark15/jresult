package uk.chaoticgoose.jresult.nonthrowing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.ResultHelpers.CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

public class ValueOrThrowTest {

    @Test
    void successReturnsValue() {
        assertThat(Result.success(VALUE).valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void failureThrows() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Result.failure(CAUSE).valueOrThrow()).withMessage("Result is a failure");
    }
}
