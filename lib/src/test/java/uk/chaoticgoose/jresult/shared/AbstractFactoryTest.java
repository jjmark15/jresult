package uk.chaoticgoose.jresult.shared;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import uk.chaoticgoose.jresult.BaseResult;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;

@NullMarked
public abstract class AbstractFactoryTest<T, E> {

    @Test
    void success() {
        assertThat(createSuccess(successValue())).hasSuccessValue(successValue());
    }

    @Test
    void failure() {
        assertThat(createFailure(failureCause())).hasFailureCause(failureCause());
    }

    @Test
    @SuppressWarnings("all")
    void successValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> createSuccess(null));
    }

    @Test
    @SuppressWarnings("all")
    void failureValueMustNotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> createFailure(null));
    }

    protected abstract BaseResult<T, E> createSuccess(T successValue);

    protected abstract BaseResult<T, E> createFailure(E failureCause);

    protected abstract T successValue();

    protected abstract E failureCause();
}
