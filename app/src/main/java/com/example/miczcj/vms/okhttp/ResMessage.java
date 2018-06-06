package com.example.miczcj.vms.okhttp;

/**
 * Created by MicZcj on 2018/6/4.
 */

public class ResMessage {
    public int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
