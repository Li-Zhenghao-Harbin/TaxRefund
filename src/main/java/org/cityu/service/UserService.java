package org.cityu.service;

import org.cityu.service.model.UserModel;

public interface UserService {
    void register(UserModel userModel);
    UserModel getUserById(Integer id);
    void changeUserInfo(UserModel userModel);
}
