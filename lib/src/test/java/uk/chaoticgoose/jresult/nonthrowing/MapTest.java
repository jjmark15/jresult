package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapTest {

    @Test
    void mappedSuccessReturnsMappedValue() {
        assertThat(aSuccess(VALUE).map(this::mapInteger)).hasSuccessValue(ANOTHER_VALUE);
    }

    @Test
    void failureReturnsCause() {
        assertThat(aFailure(CAUSE).map(this::mapInteger)).hasFailureCause(CAUSE);
    }

    private AnotherSuccessValue mapInteger(ASuccessValue ignore) {
        return ANOTHER_VALUE;
    }
}
