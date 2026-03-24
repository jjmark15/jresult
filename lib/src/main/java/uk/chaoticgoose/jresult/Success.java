package uk.chaoticgoose.jresult;

import static java.util.Objects.requireNonNull;

public record Success<T, C>(T inner) implements Result<T, C> {
    public Success {
        requireNonNull(inner);
    }
}