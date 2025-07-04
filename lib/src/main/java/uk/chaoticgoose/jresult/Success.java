package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

@NullMarked
public final class Success<T, E extends Throwable> implements Result<T, E> {
    final T value;

    Success(T value) {
        this.value = requireNonNull(value);
    }
}
