package uk.chaoticgoose.jresult;

import org.assertj.core.api.AbstractAssert;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class ResultAssert<T, E extends Exception> extends AbstractAssert<ResultAssert<T, E>, Result<T, E>> {
    protected ResultAssert(Result<T, E> actual) {
        super(actual, ResultAssert.class);
    }

    public static <T, E extends Exception> ResultAssert<T, E> assertThat(Result<T, E> actual) {
        return new ResultAssert<>(actual);
    }

    public ResultAssert<T, E> isFailure() {
        isNotNull();
        if (actual instanceof Success<T,E>) {
            failWithMessage("Expected result to be a failure");
        }
        return this;
    }

    public ResultAssert<T, E> isSuccess() {
        isNotNull();
        if (actual instanceof Failure<T,E>) {
            failWithMessage("Expected result to be a success");
        }
        return this;
    }

    public ResultAssert<T, E> hasSuccessValue(T value) {
        isNotNull();
        isSuccess();
        if (actual instanceof Success<T,E> s) {
            if (!s.value.equals(value)) {
                failWithMessage("Expected success value to be <%s> but was <%s>", value, s.value);
            }
        }
        return this;
    }

    public ResultAssert<T, E> hasFailureCause(E cause) {
        isNotNull();
        isFailure();
        if (actual instanceof Failure<T,E> f) {
            if (!f.cause.equals(cause)) {
                failWithMessage("Expected failure cause to be <%s> but was <%s>", cause, f.cause);
            }
        }
        return this;
    }
}
