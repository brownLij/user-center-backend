package com.ljiaping.usercenter.common;

/**
 * error code
 */
public enum ErrorCode {
    PARAMS_ERROR(40000, "Params error", ""),
    NULL_ERROR(40001, "Null error", ""),
    NO_AUTH(40101, "No auth", ""),
    USER_NOT_FOUND(40102, "User not found", ""),
    SYSTEM_ERROR(50000, "System error", "");

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
