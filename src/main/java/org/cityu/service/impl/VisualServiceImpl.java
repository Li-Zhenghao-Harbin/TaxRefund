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
        return null;
    }

    @Override
    public Object getItemsPie() {
        return visualMapper.getItemsPie();
    }

    @Override
    public Object getApplicationFormsPie() {
        return null;
    }

    @Override
    public Object getCustomsConfirmAmountPie() {
        return null;
    }

    @Override
    public Object getTaxRefundMethodPie() {
        return null;
    }

    @Override
    public List<Object> getMonthlyStatistic() {
        return null;
    }
}
