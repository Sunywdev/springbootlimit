package com.sunyw.xyz.exception;

import java.io.Serializable;

public class LimitException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 7345343319907375317L;

    private String code;
    private String desc;

    public LimitException(int code, String desc) {
        this.code = String.valueOf (code);
        this.desc = desc;
    }

    public LimitException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public LimitException(String message, String code, String desc) {
        super (message);
        this.code = code;
        this.desc = desc;
    }

    public LimitException(String message, Throwable cause, String code, String desc) {
        super (message, cause);
        this.code = code;
        this.desc = desc;
    }

    public LimitException(Throwable cause, String code, String desc) {
        super (cause);
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
