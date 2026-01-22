package com.yxbear.core.exception;

public class JDBCException extends CoreException {

    private static final long serialVersionUID = 1L;

    public static final int USER_NOT_FOUND = 10000;

    public static final int USER_PWD_ERR = 10001;

    private int code = -1;

    public JDBCException() {
        super();
    }

    public JDBCException(String message) {
        super(message);
    }

    public JDBCException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JDBCException(Throwable cause) {
        super(cause);
    }

    public JDBCException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean hasCode() {
        return code != -1;
    }

    public int getCode() {
        return code;
    }

}
