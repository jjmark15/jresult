package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
sealed interface BaseFailure<T, E> extends BaseResult<T, E> permits Failure, ThrowingFailure {
    E inner();
}
