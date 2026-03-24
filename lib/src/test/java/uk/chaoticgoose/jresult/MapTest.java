package uk.chaoticgoose.jresult;

import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.TestTypes.TestCause;
import uk.chaoticgoose.jresult.TestTypes.TestCause2;
import uk.chaoticgoose.jresult.TestTypes.TestValue;
import uk.chaoticgoose.jresult.TestTypes.TestValue2;

import static uk.chaoticgoose.jresult.Result.failure;
import static uk.chaoticgoose.jresult.Result.success;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.TestTypes.aCause;
import static uk.chaoticgoose.jresult.TestTypes.aValue;

public class MapTest {
    private static final TestValue VALUE = aValue();
    private static final TestValue2 VALUE_2 = new TestValue2(VALUE.value());
    private static final TestCause CAUSE = aCause();
    private static final TestCause2 CAUSE_2 = new TestCause2(CAUSE.value());
    private static final Success<TestValue, TestCause> SUCCESS = success(VALUE);
    private static final Failure<TestValue, TestCause> FAILURE = failure(CAUSE);

    @Test
    void mapSuccess_mapsSuccessValue() {
        assertThat(SUCCESS.mapSuccess(v -> new TestValue2(v.value()))).hasSuccessValue(VALUE_2);
    }

    @Test
    void mapSuccess_passesFailureCause() {
        assertThat(FAILURE.mapSuccess(v -> new TestValue2(v.value()))).hasFailureCause(CAUSE);
    }

    @Test
    void mapFailure_mapsFailureCause() {
        assertThat(FAILURE.mapFailure(c -> new TestCause2(c.value()))).hasFailureCause(CAUSE_2);
    }

    @Test
    void mapFailure_passesSuccessValue() {
        assertThat(SUCCESS.mapFailure(c -> new TestCause2(c.value()))).hasSuccessValue(VALUE);
    }

    @Test
    void map_mapsSuccessValue() {
        assertThat(SUCCESS.map(v -> new TestValue2(v.value()), c -> new TestCause2(c.value())))
            .hasSuccessValue(VALUE_2);
    }

    @Test
    void map_mapsFailureCause() {
        assertThat(FAILURE.map(v -> new TestValue2(v.value()), c -> new TestCause2(c.value())))
            .hasFailureCause(CAUSE_2);
    }
}
