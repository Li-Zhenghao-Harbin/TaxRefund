package org.cityu.service.impl;

import org.cityu.dao.SellerMapper;
import org.cityu.dao.UserMapper;
import org.cityu.dataobject.SellerDO;
import org.cityu.dataobject.UserDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    @Transactional
    public void register(UserModel userModel) {
        UserDO userDO = convertFromUserModel(userModel);
        userMapper.insert(userDO);
        if (userModel.getRole() == 1 && userModel.getSellerTaxId() != null) {
            SellerDO sellerDO = new SellerDO();
            sellerDO.setMerchantName(userModel.getName());
            sellerDO.setSellerTaxId(userModel.getSellerTaxId());
            sellerMapper.insert(sellerDO);
        }
    }

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userMapper.getUserById(id);
        return convertFromUserDO(userDO);
    }

    @Override
    public void changeUserInfo(UserModel userModel) {
        UserDO userDO = convertFromUserModel(userModel);
        userMapper.updateByPrimaryKey(userDO);
    }

    @Override
    public UserModel validateLogin(String name, String encryptPassword) throws BusinessException {
        UserDO userDO = userMapper.selectByName(name);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL, "User not exist");
        }
        UserModel userModel = convertFromUserDO(userDO);
        // check password
        if (!encryptPassword.equals(userModel.getPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL, "Password not match");
        }
        return userModel;
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
