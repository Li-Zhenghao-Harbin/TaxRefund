package org.cityu.service.impl;

import org.cityu.dao.merchant.UserMapper;
import org.cityu.dataobject.UserDO;
import org.cityu.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDO getUserMerchantById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
