package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Either;
import uk.chaoticgoose.jresult.Result;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class FlatMapTest {
    private static final FailureCause FAILURE_CAUSE = new FailureCause(1);
    private static final AnotherFailureCause ANOTHER_FAILURE_CAUSE = new AnotherFailureCause(1);
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

    private Result<String, AnotherFailureCause> aSuccessfulMapper(int value) {
        return Result.success(ANOTHER_VALUE);
    }

    private Result<String, AnotherFailureCause> aFailingMapper(int value) {
        return Result.failure(new AnotherFailureCause(value));
    }

    private FailureCauses unifyCauses(Either<? extends FailureCause, ? extends AnotherFailureCause> cause) {
        return cause.map(c -> c, c -> c);
    }
}
