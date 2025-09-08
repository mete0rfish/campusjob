package com.mete0rfish.campusjob.support.exception;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
