package com.naz1k1.model;

import lombok.Getter;

@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    // 成功（无数据）
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    // 成功（有数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 失败（默认状态码）
    public static <T> Result<T> fail() {
        return fail(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMessage());
    }

    // 失败（自定义消息）
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    // 失败（指定状态码和消息）
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
