package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ResultHelpers {
    private ResultHelpers() {}

    public static <T> Result.Success<T, FailureCause> aSuccess(T value) {
        return Result.success(value);
    }

    public static <C> Result.Failure<Integer, C> aFailure(C cause) {
        return Result.failure(cause);
    }

    public static <T> ThrowingResult.Success<T, AnException> aThrowingSuccess(T value) {
        return Result.throwingSuccess(value);
    }

    public static <C extends Exception> ThrowingResult.Failure<Integer, C> aThrowingFailure(C cause) {
        return Result.throwingFailure(cause);
    }

    @NullMarked
    public sealed interface FailureCauses {}

    @NullMarked
    public static sealed abstract class FailureExceptions extends Exception implements FailureCauses {}

    @NullMarked
    public static final class AnException extends FailureExceptions {}

    @NullMarked
    public static final class AnotherException extends FailureExceptions {}

    @NullMarked
    public record FailureCause(int i) implements FailureCauses {}

    @NullMarked
    public record AnotherFailureCause(int i) implements FailureCauses {}
}
