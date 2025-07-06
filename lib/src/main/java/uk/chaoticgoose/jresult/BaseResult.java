package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@NullMarked
sealed interface BaseResult<T, E> permits BaseSuccess, BaseFailure, Result, ThrowingResult {

    default Optional<T> value() {
        return switch (this) {
            case BaseSuccess<T, E> v -> Optional.of(v.value);
            case BaseFailure<T, E> _ -> Optional.empty();
        };
    }

    @Nullable
    default T valueOrNull() {
        return value().orElse(null);
    }

    default Optional<E> cause() {
        return switch (this) {
            case BaseSuccess<T, E> _ -> Optional.empty();
            case BaseFailure<T, E> f -> Optional.of(f.cause);
        };
    }

    @Nullable
    default E causeOrNull() {
        return cause().orElse(null);
    }

    default T orElse(T other) {
        return value().orElse(other);
    }

    default T orElseGet(Supplier<? extends T> supplier) {
        return value().orElseGet(supplier);
    }

    default boolean isSuccess() {
        return this instanceof BaseSuccess<T,E>;
    }

    default boolean isFailure() {
        return !isSuccess();
    }
}
