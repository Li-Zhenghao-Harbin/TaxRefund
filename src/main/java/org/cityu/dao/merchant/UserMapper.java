package org.cityu.dao.merchant;

import java.util.List;
import org.cityu.dataobject.UserDO;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    List<UserDO> selectAll();

    int updateByPrimaryKey(UserDO record);
}