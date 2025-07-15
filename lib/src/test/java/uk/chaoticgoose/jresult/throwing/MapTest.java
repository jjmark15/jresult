package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;

import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.*;

@NullMarked
public class MapTest {
    private static final Integer VALUE = 1;
    private static final String MAPPED_VALUE = "value";
    private static final AnException CAUSE = new AnException();

    @Test
    void mappedSuccessReturnsMappedValue() {
        assertThat(aThrowingSuccess(VALUE).map(this::mapInteger)).hasSuccessValue(MAPPED_VALUE);
    }

    @Test
    void failureReturnsCause() {
        assertThat(aThrowingFailure(CAUSE).map(this::mapInteger)).hasFailureCause(CAUSE);
    }

    private String mapInteger(Integer ignore) {
        return MAPPED_VALUE;
    }
}
