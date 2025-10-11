package uk.chaoticgoose.jresult.nonthrowing;

import org.jspecify.annotations.NullMarked;
import uk.chaoticgoose.jresult.BaseResult;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.AFailureCause;
import uk.chaoticgoose.jresult.ResultHelpers.ASuccessValue;
import uk.chaoticgoose.jresult.shared.AbstractFactoryTest;

import static uk.chaoticgoose.jresult.ResultHelpers.CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

@NullMarked
public class FactoryTest extends AbstractFactoryTest<ASuccessValue, AFailureCause> {

    @Override
    protected BaseResult<ASuccessValue, AFailureCause> createSuccess(ASuccessValue successValue) {
        return Result.success(successValue);
    }

    @Override
    protected BaseResult<ASuccessValue, AFailureCause> createFailure(AFailureCause failureCause) {
        return Result.failure(failureCause);
    }

    @Override
    protected ASuccessValue successValue() {
        return VALUE;
    }

    @Override
    protected AFailureCause failureCause() {
        return CAUSE;
    }
}
