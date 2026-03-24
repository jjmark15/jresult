package uk.chaoticgoose.jresult;

import java.util.function.Function;

import static uk.chaoticgoose.jresult.Result.failure;
import static uk.chaoticgoose.jresult.Result.success;

public final class ResultUtils {
    private ResultUtils() {}

    public static <T, C extends Exception> Result<T, C> catching(
        Class<C> clazz,
        ThrowingSupplier<? extends T, ? extends C> supplier
    ) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                return failure(clazz.cast(e));
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> Result<T, Exception> catching(ThrowingSupplier<? extends T, ? extends Exception> supplier) {
        return catching(Exception.class, supplier);
    }

    public static <T1, T2, C1, C2 extends Exception> Result<T2, C2> mapThrowing(
        Result<T1, C1> result,
        Class<C2> clazz,
        ThrowingFunction<T1, ? extends T2, ? extends C2> throwingFunction,
        Function<C1, ? extends C2> failureMapper
    ) {
        return switch (result) {
            case Success<T1, C1> s -> {
                try {
                    yield success(throwingFunction.apply(s.inner()));
                } catch (Exception e) {
                    if (clazz.isInstance(e)) {
                        yield failure(clazz.cast(e));
                    }
                    throw new RuntimeException(e);
                }
            }
            case Failure<T1, C1> f -> failure(failureMapper.apply(f.inner()));
        };
    }

    public interface ThrowingSupplier<T, E extends Exception> {
        T get() throws E;
    }

    public interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
