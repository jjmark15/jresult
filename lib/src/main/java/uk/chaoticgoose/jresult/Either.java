package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public sealed interface Either<T, U> {
    @NullMarked record Left<T, U>(T value) implements Either<T, U> {}
    @NullMarked record Right<T, U>(U value) implements Either<T, U> {}

    static <T, U> Either<T, U> left(T value) {
        return new Left<T, U>(value);
    }

    static <T, U> Either<T, U> right(U value) {
        return new Right<T, U>(value);
    }

    default <V> V map(Function<T, V> leftMapper, Function<U, V> rightMapper) {
        return switch (this) {
            case Either.Left<T, U> v -> leftMapper.apply(v.value());
            case Either.Right<T, U> v -> rightMapper.apply(v.value());
        };
    }
}
