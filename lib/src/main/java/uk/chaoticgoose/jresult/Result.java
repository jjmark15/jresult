package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
@NullMarked
public sealed interface Result<T, E extends Exception> permits Failure, Success {

    @SuppressWarnings("unchecked")
    static <T, E extends Exception> Result<T, E> catching(Class<E> clazz, ThrowingSupplier<? extends T, ? extends E> supplier) {
        try {
            T value = supplier.supply();
            return new Success<>(value);
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                return new Failure<>((E) e);
            }
            throw (RuntimeException) e;
        }
    }

    static <T> Result<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
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

    default Optional<E> exception() {
        return switch (this) {
            case Success<T, E> v -> Optional.empty();
            case Failure<T, E> f -> Optional.of(f.cause);
        };
    }

    @Nullable
    default E exceptionOrNull() {
        return exception().orElse(null);
    }

    default T valueOrThrow() throws E {
        return switch (this) {
            case Success<T, E> v -> v.value;
            case Failure<T, E> f -> throw f.cause;
        };
    }

    default <T2> Result<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case Failure<T, E> f -> new Failure<>(f.cause);
            case Success<T, E> s -> new Success<>(mapper.apply(s.value));
        };
    }

    @NullMarked
    interface ThrowingSupplier<T, E extends Exception> {
        T supply() throws E;
    }
}
