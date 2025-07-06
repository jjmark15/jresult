package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import uk.chaoticgoose.jresult.ThrowingResult.ThrowingSupplier;

import java.util.function.Function;

@SuppressWarnings("unused")
@NullMarked
public sealed interface Result<T, E> extends BaseResult<T, E> permits Success, Failure {

    static <T, E extends Exception> ThrowingResult<T, E> catching(Class<E> clazz, ThrowingSupplier<? extends T, ? extends E> supplier) {
        return ThrowingResult.catching(clazz, supplier);
    }

    static <T> ThrowingResult<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return ThrowingResult.catching(supplier);
    }

    static <T, E> Success<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E> Failure<T, E> failure(E cause) {
        return new Failure<>(cause);
    }

    static <T, E extends Exception> ThrowingSuccess<T, E> throwingSuccess(T value) {
        return ThrowingResult.success(value);
    }

    static <T, E extends Exception> ThrowingFailure<T, E> throwingFailure(E cause) {
        return ThrowingResult.failure(cause);
    }

    default <T2> Result<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case Success<T, E> s -> new Success<>(mapper.apply(s.value));
            case Failure<T, E> f -> new Failure<>(f.cause);
        };
    }
}
