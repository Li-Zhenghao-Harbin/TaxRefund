package org.cityu.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class CustomsAccessAspect {

    @Value("${server.port:8082}")
    private String serverPort;

    // catch @CustomsOnly, can only be accessed by customs through port 8082
    @Before("@annotation(org.cityu.common.annotation.CustomsOnly) || @within(org.cityu.common.annotation.CustomsOnly)")
    public void checkCustomsAccess(JoinPoint joinPoint) {
        if (!"8082".equals(serverPort)) {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Can only be accessed through 8082"
            );
        }
    }
}
