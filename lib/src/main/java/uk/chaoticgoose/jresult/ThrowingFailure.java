package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ThrowingFailure<T, E extends Exception>(E inner) implements BaseFailure<T, E>, ThrowingResult<T, E> {
}
