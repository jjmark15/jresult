package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@NullMarked
public sealed interface BaseResult<T, C> permits BaseResult.BaseSuccess, BaseResult.BaseFailure, Result, ThrowingResult {

    default Optional<T> value() {
        return switch (this) {
            case BaseSuccess<T, C> v -> Optional.of(v.inner());
            case BaseFailure<T, C> _ -> Optional.empty();
        };
    }

    @Nullable
    default T valueOrNull() {
        return value().orElse(null);
    }

    default Optional<C> cause() {
        return switch (this) {
            case BaseSuccess<T, C> _ -> Optional.empty();
            case BaseFailure<T, C> f -> Optional.of(f.inner());
        };
    }

    @Nullable
    default C causeOrNull() {
        return cause().orElse(null);
    }

    default T orElse(T other) {
        return value().orElse(other);
    }

    default T orElseGet(Supplier<? extends T> supplier) {
        return value().orElseGet(supplier);
    }

    default boolean isSuccess() {
        return this instanceof BaseSuccess<T, C>;
    }

    default boolean isFailure() {
        return !isSuccess();
    }

    @NullMarked
    sealed interface BaseSuccess<T, C> extends BaseResult<T, C> permits Result.Success, ThrowingResult.Success {
        T inner();
    }

    @NullMarked
    sealed interface BaseFailure<T, C> extends BaseResult<T, C> permits Result.Failure, ThrowingResult.Failure {
        C inner();
    }
}
