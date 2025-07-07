package uk.chaoticgoose.jresult;

import org.assertj.core.api.AbstractAssert;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnusedReturnValue"})
class ResultAssert<T, E extends Exception> extends AbstractAssert<ResultAssert<T, E>, BaseResult<T, E>> {
    protected ResultAssert(BaseResult<T, E> actual) {
        super(actual, ResultAssert.class);
    }

    public static <T, E extends Exception> ResultAssert<T, E> assertThat(BaseResult<T, E> actual) {
        return new ResultAssert<>(actual);
    }

    public ResultAssert<T, E> isFailure() {
        isNotNull();
        if (actual instanceof BaseSuccess<T,E>) {
            failWithMessage("Expected result to be a failure");
        }
        return this;
    }

    public ResultAssert<T, E> isSuccess() {
        isNotNull();
        if (actual instanceof BaseFailure<T,E>) {
            failWithMessage("Expected result to be a success");
        }
        return this;
    }

    public ResultAssert<T, E> hasSuccessValue(T value) {
        isNotNull();
        isSuccess();
        if (actual instanceof BaseSuccess<T, E> s) {
            if (!s.inner().equals(value)) {
                failWithMessage("Expected success value to be <%s> but was <%s>", value, s.inner());
            }
        }
        return this;
    }

    public ResultAssert<T, E> hasFailureCause(E cause) {
        isNotNull();
        isFailure();
        if (actual instanceof BaseFailure<T,E> f) {
            if (!f.inner().equals(cause)) {
                failWithMessage("Expected failure cause to be <%s> but was <%s>", cause, f.inner());
            }
        }
        return this;
    }
}
