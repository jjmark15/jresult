package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ThrowingFailure<T, E extends Exception> extends BaseFailure<T, E> implements ThrowingResult<T, E> {
    public ThrowingFailure(E cause) {
        super(cause);
    }
}
