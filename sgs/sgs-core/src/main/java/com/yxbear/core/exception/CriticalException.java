package com.yxbear.core.exception;


public class CriticalException extends CoreException {

    private static final long serialVersionUID = 1L;

    public CriticalException() {
        super();
    }

    public CriticalException(String message) {
        super(message);
    }

    public CriticalException(Throwable cause) {
        super(cause);
    }

    public CriticalException(String message, Throwable cause) {
        super(message, cause);
    }

}
