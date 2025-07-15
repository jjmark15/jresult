package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ResultHelpers {
    private ResultHelpers() {}

    public static final ASuccessValue VALUE = new ASuccessValue(1);
    public static final AnotherSuccessValue ANOTHER_VALUE = new AnotherSuccessValue(2);
    public static final AFailureCause CAUSE = new AFailureCause(1);
    public static final AnException THROWING_CAUSE = new AnException();
    public static final AnotherFailureCause ANOTHER_CAUSE = new AnotherFailureCause(2);
    public static final AnotherException ANOTHER_THROWING_CAUSE = new AnotherException();

    public static <T> Result.Success<T, AFailureCause> aSuccess(T value) {
        return Result.success(value);
    }

    public static <C> Result.Failure<ASuccessValue, C> aFailure(C cause) {
        return Result.failure(cause);
    }

    public static <T> ThrowingResult.Success<T, AnException> aThrowingSuccess(T value) {
        return Result.throwingSuccess(value);
    }

    public static <C extends Exception> ThrowingResult.Failure<ASuccessValue, C> aThrowingFailure(C cause) {
        return Result.throwingFailure(cause);
    }

    @NullMarked
    public sealed interface FailureCause {}

    @NullMarked
    public static sealed abstract class FailureExceptions extends Exception implements FailureCause {}

    @NullMarked
    public static final class AnException extends FailureExceptions {}

    @NullMarked
    public static final class AnotherException extends FailureExceptions {}

    @NullMarked
    public record AFailureCause(int i) implements FailureCause {}

    @NullMarked
    public record AnotherFailureCause(int i) implements FailureCause {}

    @NullMarked
    public sealed interface SuccessValue {}

    @NullMarked
    public record ASuccessValue(Integer i) implements SuccessValue {}

    @NullMarked
    public record AnotherSuccessValue(Integer i) implements SuccessValue {}
}
