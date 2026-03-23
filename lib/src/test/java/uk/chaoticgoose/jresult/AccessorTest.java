package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.Result.Failure;
import uk.chaoticgoose.jresult.Result.Success;
import uk.chaoticgoose.jresult.TestTypes.TestCause;
import uk.chaoticgoose.jresult.TestTypes.TestValue;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static uk.chaoticgoose.jresult.Result.failure;
import static uk.chaoticgoose.jresult.Result.success;
import static uk.chaoticgoose.jresult.TestTypes.*;

public class AccessorTest {
    private static final TestValue VALUE = aValue();
    private static final TestCause CAUSE = aCause();
    private static final Success<TestValue, TestCause> SUCCESS = success(VALUE);
    private static final Failure<TestValue, TestCause> FAILURE = failure(CAUSE);

    @Test
    void valueOrNull_returnsValueWhenSuccess() {
        assertThat(SUCCESS.valueOrNull()).isEqualTo(VALUE);
    }

    @Test
    void valueOrNull_returnsNullWhenFailure() {
        assertThat(FAILURE.valueOrNull()).isNull();
    }

    @Test
    void causeOrNull_returnsCauseWhenFailure() {
        assertThat(FAILURE.causeOrNull()).isEqualTo(CAUSE);
    }

    @Test
    void causeOrNull_returnsNullWhenSuccess() {
        assertThat(SUCCESS.causeOrNull()).isNull();
    }

    @Test
    void value_returnsValueWhenSuccess() {
        assertThat(SUCCESS.value()).contains(VALUE);
    }

    @Test
    void value_returnsEmptyWhenFailure() {
        assertThat(FAILURE.value()).isEmpty();
    }

    @Test
    void cause_returnsCauseWhenFailure() {
        assertThat(FAILURE.cause()).contains(CAUSE);
    }

    @Test
    void cause_returnsEmptyWhenSuccess() {
        assertThat(SUCCESS.cause()).isEmpty();
    }

    @Test
    void valueOrThrow_returnsValueWhenSuccess() {
        assertThat(SUCCESS.valueOrThrow()).isEqualTo(VALUE);
    }

    @Test
    void valueOrThrow_throwsWhenFailure() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(FAILURE::valueOrThrow);
    }

    @Test
    void causeOrThrow_returnsCauseWhenFailure() {
        assertThat(FAILURE.causeOrThrow()).isEqualTo(CAUSE);
    }

    @Test
    void causeOrThrow_throwsWhenSuccess() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(SUCCESS::causeOrThrow);
    }

    @Test
    void orElseThrow_mapped_returnsValueWhenSuccess() throws TestException {
        assertThat(SUCCESS.orElseThrow(_ -> anException())).isEqualTo(VALUE);
    }

    @Test
    void orElseThrow_mapped_throwsWhenFailure() {
        assertThatExceptionOfType(TestException.class)
            .isThrownBy(() -> FAILURE.orElseThrow(_ -> anException()));
    }

    @Test
    void orElseThrow_returnsValueWhenSuccess() {
        assertThat(SUCCESS.orElseThrow()).isEqualTo(VALUE);
    }

    @Test
    void orElseThrow_throwsWhenFailure() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(FAILURE::orElseThrow);
    }
}
