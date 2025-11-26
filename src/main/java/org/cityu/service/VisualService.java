package org.cityu.service;

import java.util.List;

public interface VisualService {
    Object getOverview();
    Object getItemsPie();
    Object getApplicationFormsPie();
    Object getCustomsConfirmAmountPie();
    Object getTaxRefundMethodPie();
    List<Object> getMonthlyStatistic();
}
