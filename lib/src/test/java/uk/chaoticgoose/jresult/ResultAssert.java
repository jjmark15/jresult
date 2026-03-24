package uk.chaoticgoose.jresult;

import org.assertj.core.api.AbstractAssert;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ResultAssert<T, C> extends AbstractAssert<ResultAssert<T, C>, Result<T, C>> {
    protected ResultAssert(Result<T, C> actual) {
        super(actual, ResultAssert.class);
    }

    public static <T, C> ResultAssert<T, C> assertThat(Result<T, C> actual) {
        return new ResultAssert<>(actual);
    }

    public ResultAssert<T, C> isFailure() {
        isNotNull();
        if (actual instanceof Success<T,C>) {
            failWithMessage("Expected result to be a failure");
        }
        return this;
    }

    public ResultAssert<T, C> isSuccess() {
        isNotNull();
        if (actual instanceof Failure<T, C>) {
            failWithMessage("Expected result to be a success");
        }
        return this;
    }

    public ResultAssert<T, C> hasSuccessValue(T value) {
        isNotNull();
        isSuccess();
        if (actual instanceof Success<T, C>(T inner)) {
            if (!inner.equals(value)) {
                failWithMessage("Expected success value to be <%s> but was <%s>", value, inner);
            }
        }
        return this;
    }

    public ResultAssert<T, C> hasFailureCause(C cause) {
        isNotNull();
        isFailure();
        if (actual instanceof Failure<T, C>(C inner)) {
            if (!inner.equals(cause)) {
                failWithMessage("Expected failure cause to be <%s> but was <%s>", cause, inner);
            }
        }
        return this;
    }
}
