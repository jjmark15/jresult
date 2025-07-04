package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
@NullMarked
public sealed interface Result<T, E extends Throwable> permits Failure, Success {

    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> Result<T, E> catching(ThrowingSupplier<? extends T, ? extends E> supplier) {
        try {
            T value = supplier.supply();
            return new Success<>(value);
        } catch (Throwable e) {
            try {
                return new Failure<>((E) e);
            } catch (ClassCastException ex) {
                if (e instanceof RuntimeException re) {
                    throw re;
                }
                throw new RuntimeException(e);
            }
        }
    }

    default Optional<T> value() {
        return switch (this) {
            case Success<T, E> v -> Optional.of(v.value);
            case Failure<T, E> f -> Optional.empty();
        };
    }

    @Nullable
    default T valueOrNull() {
        return value().orElse(null);
    }

    default Optional<E> throwable() {
        return switch (this) {
            case Success<T, E> v -> Optional.empty();
            case Failure<T, E> f -> Optional.of(f.cause);
        };
    }

    @Nullable
    default E throwableOrNull() {
        return throwable().orElse(null);
    }

    default T valueOrThrow() throws E {
        return switch (this) {
            case Success<T, E> v -> v.value;
            case Failure<T, E> f -> throw f.cause;
        };
    }

    @NullMarked
    interface ThrowingSupplier<T, E extends Throwable> {
        T supply() throws E;
    }
}
