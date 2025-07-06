package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ThrowingSuccess<T, E extends Exception> extends BaseSuccess<T, E> implements ThrowingResult<T, E> {
    ThrowingSuccess(T value) {
        super(value);
    }
}
