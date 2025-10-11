package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import uk.chaoticgoose.jresult.BaseResult;
import uk.chaoticgoose.jresult.ResultHelpers.ASuccessValue;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;
import uk.chaoticgoose.jresult.shared.AbstractFactoryTest;

import static uk.chaoticgoose.jresult.ResultHelpers.THROWING_CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

@NullMarked
public class FactoryTest extends AbstractFactoryTest<ASuccessValue, AnException> {

    @Override
    protected BaseResult<ASuccessValue, AnException> createSuccess(
        ASuccessValue successValue
    ) {
        return ThrowingResult.success(successValue);
    }

    @Override
    protected BaseResult<ASuccessValue, AnException> createFailure(AnException failureCause) {
        return ThrowingResult.failure(failureCause);
    }

    @Override
    protected ASuccessValue successValue() {
        return VALUE;
    }

    @Override
    protected AnException failureCause() {
        return THROWING_CAUSE;
    }
}
