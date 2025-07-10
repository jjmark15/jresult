package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public sealed interface ThrowingResult<T, E extends Exception> extends BaseResult<T, E> permits ThrowingResult.Success, ThrowingResult.Failure {

    @SuppressWarnings("unchecked")
    static <T, E extends Exception> ThrowingResult<T, E> catching(Class<E> clazz, ThrowingSupplier<? extends T, ? extends E> supplier) {
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

    static <T> ThrowingResult<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
    }

    static <T, E extends Exception> Success<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E extends Exception> Failure<T, E> failure(E e) {
        return new Failure<>(e);
    }

    default T valueOrThrow() throws E {
        return switch (this) {
            case ThrowingResult.Success<T, E> v -> v.inner();
            case ThrowingResult.Failure<T, E> f -> throw f.inner();
        };
    }

    default <T2> ThrowingResult<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case ThrowingResult.Success<T, E> v -> new Success<>(mapper.apply(v.inner()));
            case ThrowingResult.Failure<T, E> f -> new Failure<>(f.inner());
        };
    }

    default <E2 extends Exception> ThrowingResult<T, E2> mapFailure(Function<? super E, ? extends E2> mapper) {
        return switch (this) {
            case ThrowingResult.Success<T, E> v -> new Success<>(v.inner());
            case ThrowingResult.Failure<T, E> f -> new Failure<>(mapper.apply(f.inner()));
        };
    }

    default Result<T, E> toNonThrowing() {
        return toNonThrowing(Function.identity());
    }

    default <E2> Result<T, E2> toNonThrowing(Function<? super E, ? extends E2> mapper) {
        return switch (this) {
            case ThrowingResult.Success<T, E> v -> new Result.Success<>(v.inner());
            case ThrowingResult.Failure<T, E> v -> new  Result.Failure<>(mapper.apply(v.inner()));
        };
    }

    @NullMarked
    interface ThrowingSupplier<T, E extends Exception> {
        T supply() throws E;
    }

    @NullMarked
    record Success<T, E extends Exception>(T inner) implements BaseSuccess<T, E>, ThrowingResult<T, E> {
    }

    @NullMarked
    record Failure<T, E extends Exception>(E inner) implements BaseFailure<T, E>, ThrowingResult<T, E> {
    }
}
