package com.everyconti.every_conti.aop.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.everyconti.every_conti.common.utils.SecurityUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Slf4j
public class LoggingAop {

    @Before("execution(* com.everyconti.every_conti.modules..*Controller.*(..)) && !target(com.everyconti.every_conti.config.SpringConfig)")
    public void beforeLog(JoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        String methodName = method.getName();

        // Request 정보 가져오기
        HttpServletRequest request = getCurrentHttpRequest();

        String ip = request != null ? request.getRemoteAddr() : "unknown";
        String uri = request != null ? request.getRequestURI() : "unknown";
        String queryString = request != null ? request.getQueryString() : "";
        String username = SecurityUtil.getCurrentUsername().orElse("anonymous");

        String params = extractPathParams(request); // 직접 구현 필요하거나 생략 가능
        String query = queryString == null ? "{}" : "{" + queryString.replace("&", ", ") + "}";

        log.info("[REQUEST] Params: {} Query: {}", params, query);
        log.info("{} {} {} | {}", ip, request != null ? request.getMethod() : "UNKNOWN", uri + (queryString != null ? "?" + queryString : ""), username);
    }

    @AfterReturning(
            pointcut = "execution(* com.everyconti.every_conti.modules..*Controller.*(..)) && !target(com.everyconti.every_conti.config.SpringConfig)",
            returning = "result"
    )
    public void afterReturningLog(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = getCurrentHttpRequest();
        String uri = request != null ? request.getRequestURI() : "unknown";
        String queryString = request != null ? request.getQueryString() : "";
        String ip = request != null ? request.getRemoteAddr() : "unknown";
        String username = SecurityUtil.getCurrentUsername().orElse("anonymous");

        String returnStr = result == null ? "null" : result.getClass().getSimpleName() + "=" + result;

        log.info("{} {} {} | {}",
                request != null ? request.getMethod() : "UNKNOWN",
                uri + (queryString != null ? "?" + queryString : ""),
                ip,
                joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName() + " | " + username
//                returnStr
        );
    }

    private Method getMethod(JoinPoint JoinPoint) {
        MethodSignature signature = (MethodSignature) JoinPoint.getSignature();
        return signature.getMethod();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String extractPathParams(HttpServletRequest request) {
        if (request == null) return "{}";

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );

        if (pathVariables == null || pathVariables.isEmpty()) {
            return "{}";
        }

        String paramsStr = pathVariables.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"")
                .collect(Collectors.joining(", "));

        return "{" + paramsStr + "}";
    }
}