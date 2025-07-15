package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import uk.chaoticgoose.jresult.ThrowingResult.ThrowingSupplier;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
@NullMarked
public sealed interface Result<T, C> extends BaseResult<T, C> permits Result.Success, Result.Failure {

    static <T, C extends Exception> ThrowingResult<T, C> catching(Class<C> clazz, ThrowingSupplier<? extends T, ? extends C> supplier) {
        return ThrowingResult.catching(clazz, supplier);
    }

    static <T> ThrowingResult<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return ThrowingResult.catching(supplier);
    }

    static <T, C> Success<T, C> success(T value) {
        return new Success<>(value);
    }

    static <T, C> Failure<T, C> failure(C cause) {
        return new Failure<>(cause);
    }

    static <T, C extends Exception> ThrowingResult.Success<T, C> throwingSuccess(T value) {
        return ThrowingResult.success(value);
    }

    static <T, C extends Exception> ThrowingResult.Failure<T, C> throwingFailure(C cause) {
        return ThrowingResult.failure(cause);
    }

    default T valueOrThrow() throws NoSuchElementException {
        return switch (this) {
            case Success<T, C> v -> v.inner();
            case Failure<T, C> _ -> throw new NoSuchElementException("Result is a failure");
        };
    }

    default <T2> Result<T2, C> map(Function<T, ? extends T2> mapper) {
        return switch (this) {
            case Success<T, C> s -> new Success<>(mapper.apply(s.inner()));
            case Failure<T, C> f -> new Failure<>(f.inner());
        };
    }

    default <C2> Result<T, C2> mapFailure(Function<C, ? extends C2> mapper) {
        return switch (this) {
            case Success<T, C> v -> new Success<>(v.inner());
            case Failure<T, C> f -> new Failure<>(mapper.apply(f.inner()));
        };
    }

    default <T2, C2, C3> Result<T2, C3> flatMap(
            Function<T, Result<? extends T2, ? extends C2>> mapper,
            Function<Either<? extends C, ? extends C2>, ? extends C3> causeCombiner
    ) {
        return switch (this) {
            case Success<T, C> s -> switch (mapper.apply(s.inner())) {
                case Result.Success<? extends T2, ? extends C2> s2 -> new Success<>(s2.inner());
                case Result.Failure<? extends T2, ? extends C2> f2 -> new Failure<>(causeCombiner.apply(Either.right(f2.inner())));
            };
            case Failure<T, C> f -> new Failure<>(causeCombiner.apply(Either.left(f.inner())));
        };
    }

    default <T2> Result<T2, C> flatMap(Function<T, Result<? extends T2, ? extends C>> mapper) {
        return flatMap(mapper, cause -> cause.map(c -> c, c -> c));
    }

    default <C2 extends Exception> ThrowingResult<T, C2> toThrowing(Function<C, ? extends C2> mapper) {
        return switch (this) {
            case Result.Success<T, C> v -> new ThrowingResult.Success<>(v.inner());
            case Result.Failure<T, C> v -> new ThrowingResult.Failure<>(mapper.apply(v.inner()));
        };
    }

    @NullMarked
    record Success<T, C>(T inner) implements BaseSuccess<T, C>, Result<T, C> {
        public Success {
            requireNonNull(inner);
        }
    }

    @NullMarked
    record Failure<T, C>(C inner) implements BaseFailure<T, C>, Result<T, C> {
        public Failure {
            requireNonNull(inner);
        }
    }
}
