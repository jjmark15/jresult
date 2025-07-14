package uk.chaoticgoose.jresult;

import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class ResultHelpers {
    private ResultHelpers() {}

    static <T> Result.Success<T, FailureCause> aSuccess(T value) {
        return Result.success(value);
    }

    static <C> Result.Failure<Integer, C> aFailure(C cause) {
        return Result.failure(cause);
    }

    static <T> ThrowingResult.Success<T, AnException> aThrowingSuccess(T value) {
        return Result.throwingSuccess(value);
    }

    static <C extends Exception> ThrowingResult.Failure<Integer, C> aThrowingFailure(C cause) {
        return Result.throwingFailure(cause);
    }

    @NullMarked
    sealed interface FailureCauses {}

    @NullMarked
    static final class AnException extends Exception implements FailureCauses {}

    @NullMarked
    static final class AnotherException extends RuntimeException implements FailureCauses {}

    @NullMarked
    record FailureCause(int i) implements FailureCauses {}

    @NullMarked
    record AnotherFailureCause(int i) implements FailureCauses {
        public static AnotherFailureCause from(FailureCause failureCause) {
            return new AnotherFailureCause(failureCause.i());
        }
    }
}
