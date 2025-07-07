package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Failure<T, E>(E inner) implements BaseFailure<T, E>, Result<T, E> {
}
