package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

@NullMarked
sealed abstract class BaseSuccess<T, E> implements BaseResult<T, E> permits Success, ThrowingSuccess {
    final T value;

    BaseSuccess(T value) {
        this.value = requireNonNull(value);
    }
}
