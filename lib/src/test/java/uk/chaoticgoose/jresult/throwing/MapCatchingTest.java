package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Either;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ResultHelpers.AnotherException;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.aThrowingFailure;
import static uk.chaoticgoose.jresult.ResultHelpers.aThrowingSuccess;

@NullMarked
public abstract class MapCatchingTest {
    private static final Integer VALUE = 1;
    private static final String MAPPED_VALUE = "value";
    private static final AnException CAUSE = new AnException();
    private static final AnotherException ANOTHER_CAUSE = new AnotherException();

    @Nested
    static class WithClassAndMapperAndFailureCombinerTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(CAUSE)
                .mapCatching(AnotherException.class, this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnotherException.class, this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(ANOTHER_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnotherException.class, this::anotherNonThrowingMethod, this::identityCauseMap))
                .hasSuccessValue(MAPPED_VALUE);
        }
    }

    @Nested
    static class WithMapperAndFailureCombinerTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(CAUSE)
                .mapCatching(this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherThrowingMethod, this::identityCauseMap))
                .hasFailureCause(ANOTHER_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherNonThrowingMethod, this::identityCauseMap))
                .hasSuccessValue(MAPPED_VALUE);
        }
    }

    @Nested
    static class WithClassAndMapperTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(CAUSE)
                .mapCatching(AnException.class, this::throwingMethod))
                .hasFailureCause(CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnException.class, this::throwingMethod))
                .hasFailureCause(CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(AnException.class, this::nonThrowingMethod))
                .hasSuccessValue(MAPPED_VALUE);
        }
    }

    @Nested
    static class WithMapperTest extends MapCatchingTest {
        @Test
        void combinesOriginalAndCaughtFailure() {
            assertThat(aThrowingFailure(CAUSE)
                .mapCatching(this::anotherThrowingMethod))
                .hasFailureCause(CAUSE);
        }

        @Test
        void returnsCaughtFailure() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherThrowingMethod))
                .hasFailureCause(ANOTHER_CAUSE);
        }

        @Test
        void returnsSuccessValue() {
            assertThat(aThrowingSuccess(VALUE)
                .mapCatching(this::anotherNonThrowingMethod))
                .hasSuccessValue(MAPPED_VALUE);
        }
    }

    protected String throwingMethod(Integer ignore) throws AnException {
        throw CAUSE;
    }

    protected String anotherThrowingMethod(Integer ignore) throws AnotherException {
        throw ANOTHER_CAUSE;
    }

    @SuppressWarnings("all")
    protected String nonThrowingMethod(Integer ignore) throws AnException {
        return MAPPED_VALUE;
    }

    @SuppressWarnings("all")
    protected String anotherNonThrowingMethod(Integer ignore) throws AnotherException {
        return MAPPED_VALUE;
    }

    protected <C3 extends Exception, C1 extends C3, C2 extends C3> C3 identityCauseMap(Either<C1, C2> cause) {
        return cause.map(c -> c, c -> c);
    }
}
