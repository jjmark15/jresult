package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class FlatMapTest {
    private static final FailureCause FAILURE_CAUSE = new FailureCause(1);
    private static final AnotherFailureCause ANOTHER_FAILURE_CAUSE = new AnotherFailureCause(1);
    private static final AnException EXCEPTION = new AnException();
    private static final AnotherException ANOTHER_EXCEPTION = new AnotherException();
    private static final int VALUE = 1;
    private static final String ANOTHER_VALUE = "value";

    @Test
    void mapsSuccessToSuccess() {
        assertThat(aSuccess(VALUE).flatMap(this::aSuccessfulMapper, cause -> unifyCauses(cause)))
            .hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void mapsSuccessToFailure() {
        assertThat(aSuccess(VALUE).flatMap(this::aFailingMapper, cause -> unifyCauses(cause)))
            .hasFailureCause(ANOTHER_FAILURE_CAUSE);
    }

    @Test
    void mapsFailureToFailure() {
        assertThat(aFailure(FAILURE_CAUSE).flatMap(this::aFailingMapper, cause -> unifyCauses(cause)))
                .hasFailureCause(FAILURE_CAUSE);
    }

    @Test
    void mapsFailureToSuccess() {
        assertThat(aFailure(FAILURE_CAUSE).flatMap(this::aSuccessfulMapper, cause -> unifyCauses(cause)))
            .hasFailureCause(FAILURE_CAUSE);
    }

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

    private Result<String, AnotherFailureCause> aSuccessfulMapper(int value) {
        return Result.success(ANOTHER_VALUE);
    }

    private Result<String, AnotherFailureCause> aFailingMapper(int value) {
        return Result.failure(new AnotherFailureCause(value));
    }

    private ThrowingResult<String, AnotherException> aSuccessfulThrowingMapper(int value) {
        return Result.throwingSuccess(ANOTHER_VALUE);
    }

    private ThrowingResult<String, AnotherException> aFailingThrowingMapper(int value) {
        return Result.throwingFailure(ANOTHER_EXCEPTION);
    }

    private FailureCauses unifyCauses(Either<? extends FailureCause, ? extends AnotherFailureCause> cause) {
        return cause.map(c -> c, c -> c);
    }

    private Exception unifyThrowingCauses(Either<? extends AnException, ? extends AnotherException> cause) {
        return cause.map(c -> c, c -> c);
    }
}
