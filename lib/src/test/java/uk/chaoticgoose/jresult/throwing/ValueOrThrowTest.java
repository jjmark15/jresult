package uk.chaoticgoose.jresult.throwing;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ValueOrThrowTest {
    private static final String VALUE = "value";
    private static final Exception CAUSE = new AnException();

    @Test
    void successReturnsValue() throws Exception {
        assertThat(ThrowingResult.success(VALUE).valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void failureThrows() {
        assertThatExceptionOfType(AnException.class).isThrownBy(() -> ThrowingResult.failure(CAUSE).valueOrThrow());
    }
}
