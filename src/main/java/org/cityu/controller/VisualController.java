package org.cityu.controller;

import org.cityu.common.annotation.RequireRole;
import org.cityu.response.CommonReturnType;
import org.cityu.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visual")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class VisualController extends BaseController {

    @Autowired
    private VisualService visualService;

    @RequestMapping(value = "/getOverview", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getOverview() {
        Object result = visualService.getOverview();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getItemsPie", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getItemsPie() {
        Object result = visualService.getItemsPie();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getApplicationFormsPie", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getApplicationFormsPie() {
        Object result = visualService.getApplicationFormsPie();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getCustomsConfirmAmountPie", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getCustomsConfirmAmountPie() {
        Object result = visualService.getCustomsConfirmAmountPie();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getTaxRefundMethodPie", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getTaxRefundMethodPie() {
        Object result = visualService.getTaxRefundMethodPie();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getMonthlyStatistic", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getMonthlyStatistic() {
        List<Object> result = visualService.getMonthlyStatistic();
        return CommonReturnType.create(result);
    }
}
