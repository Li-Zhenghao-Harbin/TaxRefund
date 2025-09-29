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
public class MerchantAccessAspect {

    @Value("${server.port:8080}")
    private String serverPort;

    // catch @MerchantOnly, can only be accessed by merchant through port 8081
    @Before("@annotation(org.cityu.common.annotation.MerchantOnly) || @within(org.cityu.common.annotation.MerchantOnly)")
    public void checkMerchantAccess(JoinPoint joinPoint) {
        if (!"8081".equals(serverPort)) {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Can only be accessed through 8081"
            );
        }
    }
}
