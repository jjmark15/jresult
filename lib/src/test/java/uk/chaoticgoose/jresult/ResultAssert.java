package uk.chaoticgoose.jresult;

import org.assertj.core.api.AbstractAssert;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ResultAssert<T, C> extends AbstractAssert<ResultAssert<T, C>, BaseResult<T, C>> {
    protected ResultAssert(BaseResult<T, C> actual) {
        super(actual, ResultAssert.class);
    }

    public static <T, C> ResultAssert<T, C> assertThat(BaseResult<T, C> actual) {
        return new ResultAssert<>(actual);
    }

    public ResultAssert<T, C> isFailure() {
        isNotNull();
        if (actual instanceof BaseResult.BaseSuccess<T, C>) {
            failWithMessage("Expected result to be a failure");
        }
        return this;
    }

    public ResultAssert<T, C> isSuccess() {
        isNotNull();
        if (actual instanceof BaseResult.BaseFailure<T, C>) {
            failWithMessage("Expected result to be a success");
        }
        return this;
    }

    public ResultAssert<T, C> hasSuccessValue(T value) {
        isNotNull();
        isSuccess();
        if (actual instanceof BaseResult.BaseSuccess<T, C> s) {
            if (!s.inner().equals(value)) {
                failWithMessage("Expected success value to be <%s> but was <%s>", value, s.inner());
            }
        }
        return this;
    }

    public ResultAssert<T, C> hasFailureCause(C cause) {
        isNotNull();
        isFailure();
        if (actual instanceof BaseResult.BaseFailure<T, C> f) {
            if (!f.inner().equals(cause)) {
                failWithMessage("Expected failure cause to be <%s> but was <%s>", cause, f.inner());
            }
        }
        return this;
    }
}
