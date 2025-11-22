package org.cityu.service;

import org.cityu.error.BusinessException;
import org.cityu.service.model.UserModel;

public interface UserService {
    void register(UserModel userModel);
    UserModel getUserByName(String name);
    void changeUserInfo(UserModel userModel);
    UserModel validateLogin(String name, String encryptPassword) throws BusinessException;
    void delete(String name);
}
