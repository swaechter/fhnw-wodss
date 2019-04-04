package ch.fhnw.wodss.webapplication.exceptions;

public class QuietRuntimeException extends RuntimeException {

    public QuietRuntimeException(String message) {
        super(message);
    }

    // Swallow the stack trace and only show the error message
    // Source: https://stackoverflow.com/questions/31250598/prevent-stack-trace-logging-for-custom-exception-in-spring-boot-application
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
