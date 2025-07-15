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

    @Test
    void mapsThrowingSuccessToThrowingSuccess() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aSuccessfulThrowingMapper, this::identityCauseMap))
            .hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void mapsThrowingSuccessToThrowingFailure() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aFailingThrowingMapper, this::identityCauseMap))
            .hasFailureCause(ANOTHER_THROWING_CAUSE);
    }

    @Test
    void mapsThrowingFailureToThrowingFailure() {
        assertThat(aThrowingFailure(THROWING_CAUSE).flatMap(this::aFailingThrowingMapper, this::identityCauseMap))
                .hasFailureCause(THROWING_CAUSE);
    }

    @Test
    void mapsThrowingFailureToThrowingSuccess() {
        assertThat(aThrowingFailure(THROWING_CAUSE).flatMap(this::aSuccessfulThrowingMapper, this::identityCauseMap))
            .hasFailureCause(THROWING_CAUSE);
    }

    @Test
    void mapsThrowingSuccessToThrowingSuccess_withoutCauseCombiner() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aSameCauseSuccessfulThrowingMapper))
            .hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void mapsThrowingSuccessToThrowingFailure_withoutCauseCombiner() {
        assertThat(aThrowingSuccess(VALUE).flatMap(this::aSameCauseFailingThrowingMapper))
            .hasFailureCause(THROWING_CAUSE);
    }

    @Test
    void mapsThrowingFailureToThrowingFailure_withoutCauseCombiner() {
        assertThat(aThrowingFailure(THROWING_CAUSE).flatMap(this::aSameCauseFailingThrowingMapper))
                .hasFailureCause(THROWING_CAUSE);
    }

    @Test
    void mapsThrowingFailureToThrowingSuccess_withoutCauseCombiner() {
        assertThat(aThrowingFailure(THROWING_CAUSE).flatMap(this::aSameCauseSuccessfulThrowingMapper))
            .hasFailureCause(THROWING_CAUSE);
    }

    private ThrowingResult<AnotherSuccessValue, AnotherException> aSuccessfulThrowingMapper(ASuccessValue value) {
        return Result.throwingSuccess(ANOTHER_VALUE);
    }

    private ThrowingResult<AnotherSuccessValue, AnotherException> aFailingThrowingMapper(ASuccessValue value) {
        return Result.throwingFailure(ANOTHER_THROWING_CAUSE);
    }

    private ThrowingResult<AnotherSuccessValue, AnException> aSameCauseSuccessfulThrowingMapper(ASuccessValue value) {
        return Result.throwingSuccess(ANOTHER_VALUE);
    }

    private ThrowingResult<AnotherSuccessValue, AnException> aSameCauseFailingThrowingMapper(ASuccessValue value) {
        return Result.throwingFailure(THROWING_CAUSE);
    }

    private  <C3 extends Exception, C1 extends C3, C2 extends C3> C3 identityCauseMap(Either<C1, C2> cause) {
        return cause.map(c -> c, c -> c);
    }
}
