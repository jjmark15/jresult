package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@NullMarked
public sealed interface ThrowingResult<T, C extends Exception> extends BaseResult<T, C> permits ThrowingResult.Success, ThrowingResult.Failure {

    @SuppressWarnings("unchecked")
    static <T, C extends Exception> ThrowingResult<T, C> catching(Class<C> clazz, ThrowingSupplier<? extends T, ? extends C> supplier) {
        try {
            T value = supplier.supply();
            return new Success<>(value);
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                return new Failure<>((C) e);
            }
            throw (RuntimeException) e;
        }
    }

    static <T> ThrowingResult<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
    }

    static <T, C extends Exception> Success<T, C> success(T value) {
        return new Success<>(value);
    }

    static <T, C extends Exception> Failure<T, C> failure(C cause) {
        return new Failure<>(cause);
    }

    default T valueOrThrow() throws C {
        return switch (this) {
            case Success<T, C> v -> v.inner();
            case Failure<T, C> f -> throw f.inner();
        };
    }

    default <T2> ThrowingResult<T2, C> map(Function<T, ? extends T2> mapper) {
        return switch (this) {
            case Success<T, C> v -> new Success<>(mapper.apply(v.inner()));
            case Failure<T, C> f -> new Failure<>(f.inner());
        };
    }

    default <T2, C2 extends Exception, C3 extends Exception> ThrowingResult<T2, C3> mapCatching(
        Class<C2> clazz,
        ThrowingFunction<T, T2, C2> mapper,
        Function<Either<? extends C, ? extends C2>, ? extends C3> failureCombiner
    ) {
        return switch (this) {
            case Success<T, C> v -> switch (ThrowingResult.catching(clazz, () -> mapper.apply(v.inner()))) {
                case Success<T2, C2> v2 -> new Success<T2, C3>(v2.inner());
                case Failure<T2, C2> f2 -> new Failure<T2, C3>(failureCombiner.apply(Either.right(f2.inner())));
            };
            case Failure<T, C> f -> new Failure<T2, C3>(failureCombiner.apply(Either.left(f.inner())));
        };
    }

    default <T2, C3 extends Exception> ThrowingResult<T2, C3> mapCatching(
        ThrowingFunction<T, T2, Exception> mapper,
        Function<Either<? extends C, ? extends Exception>, ? extends C3> failureCombiner
    ) {
        return mapCatching(Exception.class, mapper, failureCombiner);
    }

    default <T2> ThrowingResult<T2, C> mapCatching(
        Class<C> clazz,
        ThrowingFunction<T, T2, C> mapper
    ) {
        return mapCatching(clazz, mapper, cause -> cause.map(c -> c, c -> c));
    }

    default <T2> ThrowingResult<T2, Exception> mapCatching(
        ThrowingFunction<T, T2, Exception> mapper
    ) {
        return mapCatching(Exception.class, mapper, cause -> cause.map(c -> c, c -> c));
    }

    default <C2 extends Exception> ThrowingResult<T, C2> mapFailure(Function<C, ? extends C2> mapper) {
        return switch (this) {
            case Success<T, C> v -> new Success<>(v.inner());
            case Failure<T, C> f -> new Failure<>(mapper.apply(f.inner()));
        };
    }

    default <T2, C2 extends Exception, C3 extends Exception> ThrowingResult<T2, C3> flatMap(
        Function<T, ThrowingResult<? extends T2, ? extends C2>> mapper,
        Function<Either<? extends C, ? extends C2>, ? extends C3> causeCombiner
    ) {
        return switch (this) {
            case Success<T, C> s -> switch (mapper.apply(s.inner())) {
                case Success<? extends T2, ? extends C2> s2 -> new Success<>(s2.inner());
                case Failure<? extends T2, ? extends C2> f2 -> new Failure<>(causeCombiner.apply(Either.right(f2.inner())));
            };
            case Failure<T, C> f -> new Failure<>(causeCombiner.apply(Either.left(f.inner())));
        };
    }

    default Result<T, C> toNonThrowing() {
        return toNonThrowing(Function.identity());
    }

    default <C2> Result<T, C2> toNonThrowing(Function<C, ? extends C2> mapper) {
        return switch (this) {
            case ThrowingResult.Success<T, C> v -> new Result.Success<>(v.inner());
            case ThrowingResult.Failure<T, C> v -> new  Result.Failure<>(mapper.apply(v.inner()));
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
    record Success<T, C extends Exception>(T inner) implements BaseSuccess<T, C>, ThrowingResult<T, C> {
        public Success {
            requireNonNull(inner);
        }
    }

    @NullMarked
    record Failure<T, C extends Exception>(C inner) implements BaseFailure<T, C>, ThrowingResult<T, C> {
        public Failure {
            requireNonNull(inner);
        }
    }
}
