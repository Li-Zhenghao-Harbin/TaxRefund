package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.ApplicationFormDO;

public interface ApplicationFormMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationFormDO record);

    ApplicationFormDO selectByPrimaryKey(Integer id);

    List<ApplicationFormDO> selectAll();

    int updateByPrimaryKey(ApplicationFormDO record);
}