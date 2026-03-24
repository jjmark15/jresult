package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.TestTypes.TestCause;
import uk.chaoticgoose.jresult.TestTypes.TestValue;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.chaoticgoose.jresult.Result.failure;
import static uk.chaoticgoose.jresult.Result.success;
import static uk.chaoticgoose.jresult.TestTypes.aCause;
import static uk.chaoticgoose.jresult.TestTypes.aValue;

public class FlagsTest {
    private static final TestValue VALUE = aValue();
    private static final TestCause CAUSE = aCause();
    private static final Success<TestValue, TestCause> SUCCESS = success(VALUE);
    private static final Failure<TestValue, TestCause> FAILURE = failure(CAUSE);

    @Test
    void isSuccessReturnsTrueWhenSuccess() {
        assertThat(SUCCESS.isSuccess()).isTrue();
    }

    @Test
    void isSuccessReturnsFalseWhenFailure() {
        assertThat(FAILURE.isSuccess()).isFalse();
    }

    @Test
    void isFailureReturnsTrueWhenSuccess() {
        assertThat(FAILURE.isFailure()).isTrue();
    }

    @Test
    void isFailureReturnsFalseWhenFailure() {
        assertThat(SUCCESS.isFailure()).isFalse();
    }
}
