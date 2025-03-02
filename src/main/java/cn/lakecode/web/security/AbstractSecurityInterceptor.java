package cn.lakecode.web.security;

import cn.lakecode.web.annotation.HasAuthority;
import cn.lakecode.web.annotation.IgnoreLogin;
import cn.lakecode.web.constant.Constants;
import cn.lakecode.web.exception.RException;
import cn.lakecode.web.resp.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public abstract class AbstractSecurityInterceptor implements HandlerInterceptor {

    public abstract Authentication validToken(String token);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // 是否不需要登录
        if (ignore(handlerMethod)) {
            return true;
        }
        String token = request.getHeader(Constants.HEADER_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RException(ResponseCode.AUTH_BAD);
        }
        Authentication authentication = validToken(token);
        if (authentication == null) {
            throw new RException(ResponseCode.AUTH_INVALID);
        }
        boolean b = validPermission(handlerMethod, authentication);
        if (b) {
            SecurityHolder.setAuthentication(authentication);
        }
        return b;
    }

    private static boolean validPermission(HandlerMethod handlerMethod, Authentication authentication) {
        // 超级管理员直接放开
        if (authentication.isSupRole()) {
            return true;
        }
        HasAuthority annotation = handlerMethod.getMethodAnnotation(HasAuthority.class);
        if (annotation == null) {
            Class<?> clazz = handlerMethod.getBeanType();
            annotation = clazz.getAnnotation(HasAuthority.class);
        }
        if (annotation == null) {
            return true;
        }
        String permission = annotation.value();
        List<String> permissions = authentication.getPermissions();
        return permissions != null && permissions.contains(permission);
    }

    private static boolean ignore(HandlerMethod handlerMethod) {
        IgnoreLogin ignoreLogin = handlerMethod.getMethodAnnotation(IgnoreLogin.class);
        if (ignoreLogin != null) {
            return true;
        }
        Class<?> clazz = handlerMethod.getBeanType();
        IgnoreLogin annotation = clazz.getAnnotation(IgnoreLogin.class);
        return annotation != null;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityHolder.clear();
    }
}
