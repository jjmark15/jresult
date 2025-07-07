package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public sealed interface ThrowingResult<T, E extends Exception> extends BaseResult<T, E> permits ThrowingResult.ThrowingSuccess, ThrowingResult.ThrowingFailure {

    @SuppressWarnings("unchecked")
    static <T, E extends Exception> ThrowingResult<T, E> catching(Class<E> clazz, ThrowingResult.ThrowingSupplier<? extends T, ? extends E> supplier) {
        try {
            T value = supplier.supply();
            return new ThrowingSuccess<>(value);
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                return new ThrowingFailure<>((E) e);
            }
            throw (RuntimeException) e;
        }
    }

    static <T> ThrowingResult<T, Exception> catching(ThrowingResult.ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
    }

    static <T, E extends Exception> ThrowingSuccess<T, E> success(T value) {
        return new ThrowingSuccess<>(value);
    }

    static <T, E extends Exception> ThrowingFailure<T, E> failure(E e) {
        return new ThrowingFailure<>(e);
    }

    default T valueOrThrow() throws E {
        return switch (this) {
            case ThrowingSuccess<T, E> v -> v.inner();
            case ThrowingFailure<T, E> f -> throw f.inner();
        };
    }

    default <T2> ThrowingResult<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case ThrowingSuccess<T, E> v -> new ThrowingSuccess<>(mapper.apply(v.inner()));
            case ThrowingFailure<T, E> f -> new ThrowingFailure<>(f.inner());
        };
    }

    default <E2 extends Exception> ThrowingResult<T, E2> mapFailure(Function<? super E, ? extends E2> mapper) {
        return switch (this) {
            case ThrowingSuccess<T, E> v -> new ThrowingSuccess<>(v.inner());
            case ThrowingFailure<T, E> f -> new ThrowingFailure<>(mapper.apply(f.inner()));
        };
    }

    @NullMarked
    interface ThrowingSupplier<T, E extends Exception> {
        T supply() throws E;
    }

    @NullMarked
    record ThrowingSuccess<T, E extends Exception>(T inner) implements BaseResult.BaseSuccess<T, E>, ThrowingResult<T, E> {
    }

    @NullMarked
    record ThrowingFailure<T, E extends Exception>(E inner) implements BaseResult.BaseFailure<T, E>, ThrowingResult<T, E> {
    }
}
