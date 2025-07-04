package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

@NullMarked
public final class Failure<T, E extends Throwable> implements Result<T, E> {
    final E cause;

    Failure(E cause) {
        this.cause = requireNonNull(cause);
    }
}
