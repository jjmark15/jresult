package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
sealed interface BaseSuccess<T, E> extends BaseResult<T, E> permits Success, ThrowingSuccess {
    T inner();
}
