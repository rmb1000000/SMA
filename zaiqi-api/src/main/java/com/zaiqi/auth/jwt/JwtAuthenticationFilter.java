package com.zaiqi.auth.jwt;

import com.zaiqi.common.ErrorCode;
import com.zaiqi.common.Result;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/")
            || path.startsWith("/api/admin/auth/")
            || path.startsWith("/api/v3/api-docs")
            || path.startsWith("/api/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            writeUnauthorized(response, ErrorCode.UNAUTHORIZED);
            return;
        }
        Boolean blacklisted = redisTemplate.hasKey("blacklist:token:" + token);
        if (Boolean.TRUE.equals(blacklisted)) {
            writeUnauthorized(response, ErrorCode.TOKEN_INVALID);
            return;
        }
        Claims claims = jwtTokenProvider.getClaims(token);
        if (claims == null) {
            writeUnauthorized(response, ErrorCode.TOKEN_INVALID);
            return;
        }
        Long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);
        if ("ADMIN".equals(role)) {
            redisTemplate.opsForValue().set("online:user:" + userId, "online", 5, TimeUnit.MINUTES);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            redisTemplate.opsForValue().set("online:user:" + userId, "online", 5, TimeUnit.MINUTES);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) return tokenParam;
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(JSON.toJSONString(Result.error(errorCode)));
    }
}
