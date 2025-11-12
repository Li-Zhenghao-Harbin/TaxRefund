package org.cityu.controller;

import org.cityu.common.annotation.CustomsOnly;
import org.cityu.response.CommonReturnType;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.model.ApplicationFormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customs")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@CustomsOnly
public class CustomsController {

    @Autowired
    ApplicationFormService applicationFormService;

    @RequestMapping(value = "/getApplicationForm", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getApplicationForm(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) {
        ApplicationFormModel applicationFormModel = applicationFormService.getApplicationForm(applicationFormNumber);
        return CommonReturnType.create(applicationFormModel);
    }
}
