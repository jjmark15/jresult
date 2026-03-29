package uk.chaoticgoose.jresult;

import java.util.UUID;

public final class TestTypes {
    private TestTypes() {}

    public record TestValue(String value) {}

    public record TestValue2(String value) {}

    public record TestCause(String value) {}

    public record TestCause2(String value) {}

    public static TestValue aValue() {
        return new TestValue(UUID.randomUUID().toString());
    }

    public static TestCause aCause() {
        return new TestCause(UUID.randomUUID().toString());
    }

    public static class TestException extends Exception {
        public TestException(String message) {
            super(message);
        }
    }

    public static TestException anException() {
        return new TestException(UUID.randomUUID().toString());
    }

    public static class TestRuntimeException extends RuntimeException {
        public TestRuntimeException(String message) {
            super(message);
        }
    }

    public static TestRuntimeException aRuntimeException() {
        return new TestRuntimeException(UUID.randomUUID().toString());
    }
}
