package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.TaxRefundDO;

public interface TaxRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaxRefundDO record);

    TaxRefundDO selectByPrimaryKey(Integer id);

    List<TaxRefundDO> selectAll();

    int updateByPrimaryKey(TaxRefundDO record);
}