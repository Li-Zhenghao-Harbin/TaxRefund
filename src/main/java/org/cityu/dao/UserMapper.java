package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.UserDO;

public interface UserMapper {
    int insert(UserDO record);

    List<UserDO> getAllUsers();

    int updateUserInfo(UserDO record);

    UserDO getUserByName(String name);

    int delete(String name);
}