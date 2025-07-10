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
            case Success<T, E> v -> v.inner();
            case Failure<T, E> f -> throw f.inner();
        };
    }

    default <T2> ThrowingResult<T2, E> map(Function<? super T, ? extends T2> mapper) {
        return switch (this) {
            case Success<T, E> v -> new Success<>(mapper.apply(v.inner()));
            case Failure<T, E> f -> new Failure<>(f.inner());
        };
    }

    default <T2, E2 extends Exception, E3 extends Exception> ThrowingResult<T2, E3> mapCatching(
            Class<E2> clazz,
            ThrowingFunction<T, T2, E2> mapper,
            Function<Either<? extends E, ? extends E2>, ? extends E3> failureCombiner
    ) {
        return switch (this) {
            case Success<T, E> v -> switch (ThrowingResult.catching(clazz, () -> mapper.apply(v.inner()))) {
                case Success<T2, E2> v2 -> new Success<T2, E3>(v2.inner());
                case Failure<T2, E2> f2 -> new Failure<T2, E3>(failureCombiner.apply(Either.right(f2.inner())));
            };
            case Failure<T, E> f -> new Failure<T2, E3>(failureCombiner.apply(Either.left(f.inner())));
        };
    }

    default <T2, E3 extends Exception> ThrowingResult<T2, E3> mapCatching(
            ThrowingFunction<T, T2, Exception> mapper,
            Function<Either<? extends E, ? extends Exception>, ? extends E3> failureCombiner
    ) {
        return mapCatching(Exception.class, mapper, failureCombiner);
    }

    default <E2 extends Exception> ThrowingResult<T, E2> mapFailure(Function<? super E, ? extends E2> mapper) {
        return switch (this) {
            case Success<T, E> v -> new Success<>(v.inner());
            case Failure<T, E> f -> new Failure<>(mapper.apply(f.inner()));
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
    interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @NullMarked
    record Success<T, E extends Exception>(T inner) implements BaseSuccess<T, E>, ThrowingResult<T, E> {
    }

    @NullMarked
    record Failure<T, E extends Exception>(E inner) implements BaseFailure<T, E>, ThrowingResult<T, E> {
    }
}
