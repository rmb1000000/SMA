package com.zaiqi.member.annotation;

import com.alibaba.fastjson2.JSON;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.common.Result;
import com.zaiqi.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MemberLevelInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequireMemberLevel annotation = handlerMethod.getMethodAnnotation(RequireMemberLevel.class);
        if (annotation == null) {
            annotation = handlerMethod.getBeanType().getAnnotation(RequireMemberLevel.class);
        }
        if (annotation == null) return true;

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            writeForbidden(response, "请先登录");
            return false;
        }

        boolean hasPermission = memberService.hasPermission(userId, annotation.value());
        if (!hasPermission) {
            writeForbidden(response, annotation.message());
            return false;
        }
        return true;
    }

    private void writeForbidden(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(JSON.toJSONString(
                Result.error(ErrorCode.FORBIDDEN.getCode(), message)));
    }
}
