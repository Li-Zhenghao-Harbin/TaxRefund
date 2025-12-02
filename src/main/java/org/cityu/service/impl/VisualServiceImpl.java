package org.cityu.service.impl;

import org.cityu.dao.VisualMapper;
import org.cityu.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisualServiceImpl implements VisualService {

    @Autowired
    private VisualMapper visualMapper;

    @Override
    public Object getOverview() {
        return visualMapper.getOverview();
    }

    @Override
    public Object getItemsPie() {
        return visualMapper.getItemsPie();
    }

    @Override
    public Object getApplicationFormsPie() {
        return visualMapper.getApplicationFormsPie();
    }

    @Override
    public Object getCustomsConfirmAmountPie() {
        return visualMapper.getCustomsConfirmAmountPie();
    }

    @Override
    public Object getTaxRefundMethodPie() {
        return visualMapper.getTaxRefundMethodPie();
    }

    @Override
    public List<Object> getMonthlyStatistic() {
        return visualMapper.getMonthlyStatistic();
    }
}
