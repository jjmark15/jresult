package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Success<T, E> extends BaseSuccess<T, E> implements Result<T, E> {
    Success(T value) {
        super(value);
    }
}
