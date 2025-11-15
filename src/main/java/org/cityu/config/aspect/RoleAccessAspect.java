package org.cityu.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class RoleAccessAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Before("@annotation(org.cityu.common.annotation.RequireRole)")
    public void checkRoleAccess(JoinPoint joinPoint) {
        // check if token is existed
        String token = getTokenFromRequest();
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token not exist");
        }
        // validate token
        if (!jwtTokenUtils.validateToken(token) || jwtTokenUtils.isTokenExpired(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        // check user role
        Integer currentUserRole = jwtTokenUtils.getRoleFromToken(token);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole != null) {
            String[] allowedRoleStrings = requireRole.value();
            if (allowedRoleStrings.length > 0) {
                boolean hasRole = Arrays.stream(allowedRoleStrings)
                        .map(Integer::parseInt)
                        .anyMatch(role -> role.equals(currentUserRole));
                if (!hasRole) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }
        }
    }

    private String getTokenFromRequest() {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            } else {
                return authHeader;
            }
        }
        return null;
    }
}