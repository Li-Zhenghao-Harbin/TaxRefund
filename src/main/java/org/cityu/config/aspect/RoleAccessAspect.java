package org.cityu.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.cityu.common.annotation.RequireRole;
import org.cityu.service.model.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class RoleAccessAspect {

    @Before("@annotation(org.cityu.common.annotation.RequireRole)")
    public void checkRoleAccess(JoinPoint joinPoint) {
        // get current user role
        Integer currentUserRole = getCurrentUserRole();
        if (currentUserRole == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not login");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole != null) {
            String[] allowedRoleStrings = requireRole.value();
            if (allowedRoleStrings.length > 0) {
                // convert role from string to integer
                boolean hasRole = Arrays.stream(allowedRoleStrings)
                        .map(Integer::parseInt)  // convert
                        .anyMatch(role -> role.equals(currentUserRole));
                if (!hasRole) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }
        }
    }

    private Integer getCurrentUserRole() {
        // get role from session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UserModel role = (UserModel) request.getSession().getAttribute("role");
        if (role != null) {
            return role.getRole();
        }
//        return null;
        // TODO
        // debug
        return new Integer(3);
    }
}