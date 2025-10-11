package uk.chaoticgoose.jresult.throwing;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.chaoticgoose.jresult.Result;
import uk.chaoticgoose.jresult.ResultHelpers.ASuccessValue;
import uk.chaoticgoose.jresult.ResultHelpers.AnException;
import uk.chaoticgoose.jresult.ThrowingResult;
import uk.chaoticgoose.jresult.ThrowingResult.ThrowingSupplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static uk.chaoticgoose.jresult.ResultAssert.assertThat;
import static uk.chaoticgoose.jresult.ResultHelpers.THROWING_CAUSE;
import static uk.chaoticgoose.jresult.ResultHelpers.VALUE;

@NullMarked
public class CatchingTest {

    @ParameterizedTest
    @MethodSource("catchingBiFunctions")
    void handlesNonThrowingAction(
        BiFunction<Class<AnException>, ThrowingSupplier<ASuccessValue, AnException>, ThrowingResult<ASuccessValue, AnException>> catchingBiFunction
    ) {
        assertThat(catchingBiFunction.apply(AnException.class, this::nonThrowingMethod)).hasSuccessValue(VALUE);
    }

    @ParameterizedTest
    @MethodSource("catchingBiFunctions")
    void catchesThrowingAction(
        BiFunction<Class<AnException>, ThrowingSupplier<ASuccessValue, AnException>, ThrowingResult<ASuccessValue, AnException>> catchingBiFunction
    ) {
        assertThat(catchingBiFunction.apply(AnException.class, this::throwingMethod)).hasFailureCause(THROWING_CAUSE);
    }

    @ParameterizedTest
    @MethodSource("catchingBiFunctions")
    void doesNotCatchExceptionsOfDifferentType(
        BiFunction<Class<AnException>, ThrowingSupplier<Integer, AnException>, ThrowingResult<Integer, AnException>> catchingBiFunction
    ) {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            ThrowingResult<Integer, AnException> _ = catchingBiFunction.apply(AnException.class, this::runtimeThrowingMethod);
        });
    }

    @ParameterizedTest
    @MethodSource("catchingFunctions")
    void catchesBaseCause(
        Function<ThrowingSupplier<ASuccessValue, Exception>, ThrowingResult<ASuccessValue, Exception>> catchingFunction
    ) {
        assertThat(catchingFunction.apply(this::throwingMethod)).hasFailureCause(THROWING_CAUSE);
    }

    private <T> T throwingMethod() throws AnException {
        throw THROWING_CAUSE;
    }

    private <T> T runtimeThrowingMethod() throws RuntimeException {
        throw new RuntimeException();
    }

    private ASuccessValue nonThrowingMethod() {
        return VALUE;
    }

    private static <T, E extends Exception> Stream<Arguments> catchingBiFunctions() {
        BiFunction<Class<E>, ThrowingSupplier<T, E>, ThrowingResult<T, E>> f1 = Result::catching;
        BiFunction<Class<E>, ThrowingSupplier<T, E>, ThrowingResult<T, E>> f2 = ThrowingResult::catching;

        return Stream.of(arguments(f1), arguments(f2));
    }

    private static <T> Stream<Arguments> catchingFunctions() {
        Function<ThrowingSupplier<T, Exception>, ThrowingResult<T, Exception>> f1 = Result::catching;
        Function<ThrowingSupplier<T, Exception>, ThrowingResult<T, Exception>> f2 = ThrowingResult::catching;

        return Stream.of(arguments(f1), arguments(f2));
    }
}
