package org.cityu.service.impl;

import org.cityu.dao.UserMapper;
import org.cityu.dataobject.UserDO;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(UserModel userModel) {
        UserDO userDO = convertFromUserModel(userModel);
        userMapper.insert(userDO);
    }

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userMapper.selectByPrimaryKey(id);
        return convertFromUserDO(userDO);
    }

    @Override
    public void changeUserInfo(UserModel userModel) {
        UserDO userDO = convertFromUserModel(userModel);
        userMapper.updateByPrimaryKey(userDO);
    }

    private UserDO convertFromUserModel(UserModel userModel) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserModel convertFromUserDO(UserDO userDO) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        return userModel;
    }
}
