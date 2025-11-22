package org.cityu.common.intercepter;

import org.cityu.common.component.UserContext;
import org.cityu.common.utils.JwtTokenUtils;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // get token from request
        String token = getTokenFromRequest(request);
        if (token != null && jwtTokenUtils.validateToken(token)) {
            String userName = jwtTokenUtils.getUserNameFromToken(token);
            if (userName != null) {
                UserModel userModel = userService.getUserByName(userName);
                if (userModel != null) {
                    UserContext.setCurrentUser(userModel);
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // clear ThreadLocal
        UserContext.clear();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
