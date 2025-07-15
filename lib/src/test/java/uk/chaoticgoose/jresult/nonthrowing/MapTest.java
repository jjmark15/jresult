package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.FailureCauses;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.aFailure;
import static uk.chaoticgoose.jresult.ResultHelpers.aSuccess;

@NullMarked
public class MapTest {
    private static final Integer VALUE = 1;
    private static final String MAPPED_VALUE = "value";
    private static final FailureCauses CAUSE = new FailureCause(1);

    @Test
    void mappedSuccessReturnsMappedValue() {
        assertThat(aSuccess(VALUE).map(this::mapInteger)).hasSuccessValue(MAPPED_VALUE);
    }

    @Test
    void failureReturnsCause() {
        assertThat(aFailure(CAUSE).map(this::mapInteger)).hasFailureCause(CAUSE);
    }

    private String mapInteger(Integer ignore) {
        return MAPPED_VALUE;
    }
}
