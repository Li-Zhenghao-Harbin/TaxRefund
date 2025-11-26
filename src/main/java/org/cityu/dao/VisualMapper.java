package org.cityu.dao;

import java.util.List;

public interface VisualMapper {
    Object getOverview();
    Object getItemsPie();
    Object getApplicationFormsPie();
    Object getCustomsConfirmAmountPie();
    Object getTaxRefundMethodPie();
    List<Object> getMonthlyStatistic();
}
