package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.BankCardDO;

public interface BankCardMapper {
    int insert(BankCardDO record);
    List<BankCardDO> selectAll();
}