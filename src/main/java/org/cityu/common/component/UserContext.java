package org.cityu.common.component;

import org.cityu.service.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private static final ThreadLocal<UserModel> CURRENT_USER = new ThreadLocal<>();

    public static UserModel getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void setCurrentUser(UserModel userModel) {
        CURRENT_USER.set(userModel);
    }

    public static String getCurrentUserName() {
        UserModel user = getCurrentUser();
        return user != null ? user.getName() : null;
    }

    public static Integer getCurrentUserRole() {
        UserModel user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
