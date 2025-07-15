package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Either;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public abstract class MapCatchingTest {

    @Nested
    static class WithClassAndMapperAndFailureCombinerTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(THROWING_CAUSE)
                .mapCatching(AnotherException.class, this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(THROWING_CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnotherException.class, this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(ANOTHER_THROWING_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnotherException.class, this::anotherNonThrowingMethod, this::identityCauseMap))
                .hasSuccessValue(ANOTHER_VALUE);
        }
    }

    @Nested
    static class WithMapperAndFailureCombinerTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(THROWING_CAUSE)
                .mapCatching(this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(THROWING_CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(ANOTHER_THROWING_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherNonThrowingMethod, this::identityCauseMap))
                .hasSuccessValue(ANOTHER_VALUE);
        }
    }

    @Nested
    static class WithClassAndMapperTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(THROWING_CAUSE)
                .mapCatching(AnException.class, this::throwingMethod))
                .hasFailureCause(THROWING_CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnException.class, this::throwingMethod))
                .hasFailureCause(THROWING_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnException.class, this::nonThrowingMethod))
                .hasSuccessValue(ANOTHER_VALUE);
        }
    }

    @Nested
    static class WithMapperTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(THROWING_CAUSE)
                .mapCatching(this::anotherThrowingMethod))
                .hasFailureCause(THROWING_CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherThrowingMethod))
                .hasFailureCause(ANOTHER_THROWING_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherNonThrowingMethod))
                .hasSuccessValue(ANOTHER_VALUE);
        }
    }

    protected AnotherSuccessValue throwingMethod(ASuccessValue ignore) throws AnException {
        throw THROWING_CAUSE;
    }

    protected AnotherSuccessValue anotherThrowingMethod(ASuccessValue ignore) throws AnotherException {
        throw ANOTHER_THROWING_CAUSE;
    }

    @SuppressWarnings("all")
    protected AnotherSuccessValue nonThrowingMethod(ASuccessValue ignore) throws AnException {
        return ANOTHER_VALUE;
    }

    @SuppressWarnings("all")
    protected AnotherSuccessValue anotherNonThrowingMethod(ASuccessValue ignore) throws AnotherException {
        return ANOTHER_VALUE;
    }

    protected <C3 extends Exception, C1 extends C3, C2 extends C3> C3 identityCauseMap(Either<C1, C2> cause) {
        return cause.map(c -> c, c -> c);
    }
}
