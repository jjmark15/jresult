package uk.chaoticgoose.jresult;

public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}