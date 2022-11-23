package com.mashibing.internalcommon.constart;


public enum CommonStatusEnum {
    VERIFICATION_CODE_ERROR(1099, "验证码错误"),
    SUCCESS(1, "success"),
    FAIL(0, "fail"),

    TOKEN_ERROR(2199, "token error");

    private int code;
    private String Value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    CommonStatusEnum(int code, String value) {
        this.code = code;
        Value = value;
    }
}
