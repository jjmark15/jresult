package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Either;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ThrowingResult;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class FlatMapTest {
    private static final AnException EXCEPTION = new AnException();
    private static final AnotherException ANOTHER_EXCEPTION = new AnotherException();
    private static final int VALUE = 1;
    private static final String ANOTHER_VALUE = "value";

    @Test
    void mapsThrowingSuccessToThrowingSuccess() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aSuccessfulThrowingMapper, cause -> unifyThrowingCauses(cause)))
            .hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void mapsThrowingSuccessToThrowingFailure() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aFailingThrowingMapper, cause -> unifyThrowingCauses(cause)))
            .hasFailureCause(ANOTHER_EXCEPTION);
    }

    @Test
    void mapsThrowingFailureToThrowingFailure() {
        assertThat(aThrowingFailure(EXCEPTION).flatMap(this::aFailingThrowingMapper, cause -> unifyThrowingCauses(cause)))
                .hasFailureCause(EXCEPTION);
    }

    @Test
    void mapsThrowingFailureToThrowingSuccess() {
        assertThat(aThrowingFailure(EXCEPTION).flatMap(this::aSuccessfulThrowingMapper, cause -> unifyThrowingCauses(cause)))
            .hasFailureCause(EXCEPTION);
    }

    private ThrowingResult<String, AnotherException> aSuccessfulThrowingMapper(int value) {
        return Result.throwingSuccess(ANOTHER_VALUE);
    }

    private ThrowingResult<String, AnotherException> aFailingThrowingMapper(int value) {
        return Result.throwingFailure(ANOTHER_EXCEPTION);
    }

    private Exception unifyThrowingCauses(Either<? extends AnException, ? extends AnotherException> cause) {
        return cause.map(c -> c, c -> c);
    }
}
