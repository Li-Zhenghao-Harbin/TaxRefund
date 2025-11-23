package org.cityu.controller;

import org.cityu.common.utils.CommonUtils;
import org.cityu.common.utils.JwtTokenUtils;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.response.CommonReturnType;
import org.cityu.service.CodeService;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cityu.controller.BaseController.CONTENT_TYPE_FORMED;

@RestController
@RequestMapping("/code")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class CodeController {

    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/getUserRoles", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getUserRoles() {
        List<Object> result = codeService.getUserRoles();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getTaxRefundMethods", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTaxRefundMethods() {
        List<Object> result = codeService.getTaxRefundMethods();
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/getStatus", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getStatus(@RequestParam(name = "business") String business) {
        List<Object> result = codeService.getStatus(business);
        return CommonReturnType.create(result);
    }
}
