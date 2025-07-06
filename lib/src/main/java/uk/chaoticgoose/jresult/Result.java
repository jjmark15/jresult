package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
@NullMarked
public sealed interface Result<T, E> extends BaseResult<T, E> permits Failure, Success {

    static <T, E extends Exception> ThrowingResult<T, E> catching(Class<E> clazz, ThrowingResult.ThrowingSupplier<? extends T, ? extends E> supplier) {
        return ThrowingResult.catching(clazz, supplier);
    }

    static <T> ThrowingResult<T, Exception> catching(ThrowingResult.ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return ThrowingResult.catching(supplier);
    }

    static <T, E> Success<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E> Failure<T, E> failure(E cause) {
        return new Failure<>(cause);
    }

    static <T, E extends Exception> ThrowingSuccess<T, E> throwingSuccess(T value) {
        return new ThrowingSuccess<>(value);
    }

    static <T, E extends Exception> ThrowingFailure<T, E> throwingFailure(E cause) {
        return new ThrowingFailure<>(cause);
    }

    default Optional<T> value() {
        return switch (this) {
            case Success<T, E> v -> Optional.of(v.value);
            case Failure<T, E> f -> Optional.empty();
        };
    }

    default Optional<E> cause() {
        return switch (this) {
            case Success<T, E> v -> Optional.empty();
            case Failure<T, E> f -> Optional.of(f.cause);
        };
    }

    default <T2> Result<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case Failure<T, E> f -> new Failure<>(f.cause);
            case Success<T, E> s -> new Success<>(mapper.apply(s.value));
        };
    }
}
