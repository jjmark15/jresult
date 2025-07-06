package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@NullMarked
public interface BaseResult<T, E> {

    Optional<T> value();

    @Nullable
    default T valueOrNull() {
        return value().orElse(null);
    }

    Optional<E> cause();

    @Nullable
    default E causeOrNull() {
        return cause().orElse(null);
    }

    <T2> BaseResult<T2, E> map(Function<? super T, ? extends T2> mapper);

    default T orElse(T other) {
        return value().orElse(other);
    }

    default T orElseGet(Supplier<? extends T> supplier) {
        return value().orElseGet(supplier);
    }
}
