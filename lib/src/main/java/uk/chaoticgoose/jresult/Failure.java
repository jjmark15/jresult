package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Failure<T, E> extends BaseFailure<T, E> implements Result<T, E> {
    Failure(E cause) {
        super(cause);
    }
}
