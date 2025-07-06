package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

@NullMarked
sealed abstract class BaseFailure<T, E> implements BaseResult<T, E> permits Failure, ThrowingFailure {
    final E cause;

    BaseFailure(E cause) {
        this.cause = requireNonNull(cause);
    }
}
