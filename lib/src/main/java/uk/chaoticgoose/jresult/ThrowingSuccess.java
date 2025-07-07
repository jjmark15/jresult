package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ThrowingSuccess<T, E extends Exception>(T inner) implements BaseSuccess<T, E>, ThrowingResult<T, E> {
}
