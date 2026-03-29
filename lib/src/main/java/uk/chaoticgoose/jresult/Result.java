package uk.chaoticgoose.jresult;

import org.jspecify.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<T, C> permits Success, Failure {

    static <T, C> Success<T, C> success(T value) {
        return new Success<>(value);
    }

    static <T, C> Failure<T, C> failure(C cause) {
        return new Failure<>(cause);
    }

    default @Nullable T valueOrNull() {
        return switch (this) {
            case Success<T, C> r -> r.inner();
            case Failure<T, C> _ -> null;
        };
    }

    default @Nullable C causeOrNull() {
        return switch (this) {
            case Success<T, C> _ -> null;
            case Failure<T, C> r -> r.inner();
        };
    }

    default T valueOrThrow() {
        return switch (this) {
            case Success<T, C> r -> r.inner();
            case Failure<T, C> _ -> throw new NoSuchElementException("Result is a failure");
        };
    }

    default C causeOrThrow() {
        return switch (this) {
            case Success<T, C> _ -> throw new NoSuchElementException("Result is a success");
            case Failure<T, C> r -> r.inner();
        };
    }

    default Optional<T> value() {
        return switch (this) {
            case Success<T, C> r -> Optional.of(r.inner());
            case Failure<T, C> _ -> Optional.empty();
        };
    }

    default Optional<C> cause() {
        return switch (this) {
            case Success<T, C> _ -> Optional.empty();
            case Failure<T, C> r -> Optional.of(r.inner());
        };
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default <E extends Exception> T orElseThrow(Function<C, E> function) throws E {
        return switch (this) {
            case Success<T, C> r -> r.inner();
            case Failure<T, C> r -> throw function.apply(r.inner());
        };
    }

    default T orElseThrow() throws NoSuchElementException {
        return orElseThrow(c -> new NoSuchElementException("Result is a failure"));
    }

    default <T2, C2> Result<T2, C2> map(Function<T, ? extends T2> successMapper, Function<C, ? extends C2> failureMapper) {
        return switch (this) {
            case Success<T, C> r -> success(successMapper.apply(r.inner()));
            case Failure<T, C> r -> failure(failureMapper.apply(r.inner()));
        };
    }

    default <T2> Result<T2, C> mapSuccess(Function<T, ? extends T2> successMapper) {
        return map(successMapper, c -> c);
    }

    default <C2> Result<T, C2> mapFailure(Function<C, ? extends C2> failureMapper) {
        return map(v -> v, failureMapper);
    }

    static <T, C extends Exception> Result<T, C> catching(
        Class<C> clazz,
        ThrowingSupplier<? extends T, ? extends C> supplier
    ) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                return failure(clazz.cast(e));
            }
            throw new RuntimeException(e);
        }
    }

    static <T> Result<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
    }

    static <T1, T2, C1, C2 extends Exception> Result<T2, C2> mapThrowing(
        Result<? extends T1, ? extends C1> result,
        Class<C2> clazz,
        ThrowingFunction<T1, ? extends T2, ? extends C2> throwingFunction,
        Function<C1, ? extends C2> failureMapper
    ) {
        return switch (result) {
            case Success<? extends T1, ? extends C1> s -> {
                try {
                    yield success(throwingFunction.apply(s.inner()));
                } catch (Exception e) {
                    if (clazz.isInstance(e)) {
                        yield failure(clazz.cast(e));
                    }
                    throw new RuntimeException(e);
                }
            }
            case Failure<? extends T1, ? extends C1> f -> failure(failureMapper.apply(f.inner()));
        };
    }
}
