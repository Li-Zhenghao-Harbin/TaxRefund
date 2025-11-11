package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.SellerDO;

public interface SellerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerDO record);

    SellerDO selectByPrimaryKey(Integer id);

    List<SellerDO> selectAll();

    int updateByPrimaryKey(SellerDO record);
}