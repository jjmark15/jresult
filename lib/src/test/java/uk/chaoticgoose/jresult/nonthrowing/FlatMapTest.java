package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Either;
import uk.chaoticgoose.jresult.Result;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class FlatMapTest {

    @Test
    void mapsSuccessToSuccess() {
        assertThat(aSuccess(VALUE).flatMap(this::aSuccessfulMapper, this::identityCauseMap))
            .hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void mapsSuccessToFailure() {
        assertThat(aSuccess(VALUE).flatMap(this::aFailingMapper, this::identityCauseMap))
            .hasFailureCause(ANOTHER_CAUSE);
    }

    @Test
    void mapsFailureToFailure() {
        assertThat(aFailure(CAUSE).flatMap(this::aFailingMapper, this::identityCauseMap))
                .hasFailureCause(CAUSE);
    }

    @Test
    void mapsFailureToSuccess() {
        assertThat(aFailure(CAUSE).flatMap(this::aSuccessfulMapper, this::identityCauseMap))
            .hasFailureCause(CAUSE);
    }

    private Result<AnotherSuccessValue, AnotherFailureCause> aSuccessfulMapper(ASuccessValue value) {
        return Result.success(ANOTHER_VALUE);
    }

    private Result<AnotherSuccessValue, AnotherFailureCause> aFailingMapper(ASuccessValue value) {
        return Result.failure(ANOTHER_CAUSE);
    }

    private <C3, C1 extends C3, C2 extends C3> C3 identityCauseMap(Either<C1, C2> cause) {
        return cause.map(c -> c, c -> c);
    }
}
