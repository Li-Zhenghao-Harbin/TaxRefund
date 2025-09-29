package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.ItemDO;

public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    List<ItemDO> selectAll();

    int updateByPrimaryKey(ItemDO record);
}