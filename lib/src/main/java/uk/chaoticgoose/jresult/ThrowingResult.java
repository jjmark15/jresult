package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public sealed interface ThrowingResult<T, E extends Exception> extends BaseResult<T, E> permits ThrowingSuccess, ThrowingFailure {

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

    default T valueOrThrow() throws E {
        return switch (this) {
            case ThrowingSuccess<T, E> v -> v.value;
            case ThrowingFailure<T, E> f -> throw f.cause;
        };
    }

    @Override
    default <T2> ThrowingResult<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case ThrowingSuccess<T, E> v -> new ThrowingSuccess<>(mapper.apply(v.value));
            case ThrowingFailure<T, E> f -> new ThrowingFailure<>(f.cause);
        };
    }

    @NullMarked
    interface ThrowingSupplier<T, E extends Exception> {
        T supply() throws E;
    }
}
