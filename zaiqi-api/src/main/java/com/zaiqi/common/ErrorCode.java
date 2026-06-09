package com.zaiqi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或 token 已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_DISABLED(1002, "用户已被禁用"),
    PHONE_EXISTS(1003, "手机号已注册"),
    LOGIN_FAILED(1005, "登录失败，账号或密码错误"),
    TOKEN_EXPIRED(1006, "token 已过期"),
    TOKEN_INVALID(1007, "token 无效"),
    SMS_CODE_ERROR(1008, "短信验证码错误"),
    SMS_CODE_EXPIRED(1009, "短信验证码已过期"),
    VERIFICATION_FAILED(1010, "实名认证失败");

    private final int code;
    private final String message;
}
