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

import java.util.ArrayList;
import java.util.List;

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
        if (userModel.getRole() == 1) {
            SellerDO sellerDO = new SellerDO();
            sellerDO.setCompany(userModel.getCompany());
            sellerDO.setMerchantName(userModel.getName());
            sellerDO.setSellerTaxId(userModel.getSellerTaxId());
            sellerMapper.insert(sellerDO);
        }
    }

    @Override
    public UserModel getUserByName(String name) {
        UserDO userDO = userMapper.getUserByName(name);
        return convertFromUserDO(userDO);
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserDO> userDOs = userMapper.getAllUsers();
        List<UserModel> userModels = convertFromUserDOs(userDOs);
        return userModels;
    }

    private List<UserModel> convertFromUserDOs(List<UserDO> userDOs) {
        List<UserModel> userModels = new ArrayList<>();
        for (UserDO userDO : userDOs) {
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userDO, userModel);
            userModels.add(userModel);
        }
        return userModels;
    }

    @Override
    @Transactional
    public void updateUser(UserModel userModel) {
        UserDO userDO = convertFromUserModel(userModel);
        if (userDO.getRole() == 1) {
            SellerDO sellerDO = new SellerDO();
            sellerDO.setMerchantName(userModel.getName());
            sellerDO.setCompany(userModel.getCompany());
            sellerDO.setSellerTaxId(userModel.getSellerTaxId());
            sellerMapper.updateSellerInfo(sellerDO);
        }
        userMapper.updateUserInfo(userDO);
    }

    @Override
    public UserModel validateLogin(String name, String encryptPassword) throws BusinessException {
        UserDO userDO = userMapper.getUserByName(name);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        if (userDO.getStatus() == 0) {
            throw new BusinessException(EmBusinessError.USER_UNAVAILABLE);
        }
        UserModel userModel = convertFromUserDO(userDO);
        // check password
        if (!encryptPassword.equals(userModel.getPassword())) {
            throw new BusinessException(EmBusinessError.PASSWORD_NOT_MATCH);
        }
        return userModel;
    }

    @Override
    public void delete(String name) {
        userMapper.delete(name);
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
