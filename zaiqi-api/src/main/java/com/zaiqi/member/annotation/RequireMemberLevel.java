package com.zaiqi.member.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireMemberLevel {
    String value();
    String message() default "请开通高级会员后使用此功能";
}
