package uk.chaoticgoose.jresult;

import static java.util.Objects.requireNonNull;

public record Failure<T, C>(C inner) implements Result<T, C> {
    public Failure {
        requireNonNull(inner);
    }
}