package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Success<T, E>(T inner) implements BaseSuccess<T, E>, Result<T, E> {
}
