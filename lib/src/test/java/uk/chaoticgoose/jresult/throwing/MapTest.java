package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapTest {

    @Test
    void mappedSuccessReturnsMappedValue() {
        assertThat(aThrowingSuccess(VALUE).map(this::mapInteger)).hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void failureReturnsCause() {
        assertThat(aThrowingFailure(THROWING_CAUSE).map(this::mapInteger)).hasFailureCause(THROWING_CAUSE);
    }

    private AnotherSuccessValue mapInteger(ASuccessValue ignore) {
        return ANOTHER_VALUE;
    }
}
