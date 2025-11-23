package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.SellerDO;

public interface SellerMapper {
    int insert(SellerDO record);
    int updateSellerInfo(SellerDO sellerDO);
}