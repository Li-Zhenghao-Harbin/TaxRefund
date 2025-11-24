package org.cityu.controller;

import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.common.utils.JwtTokenUtils;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.response.CommonReturnType;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cityu.controller.BaseController.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType register(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "role") Integer role,
                                     @RequestParam(name = "company") String company,
                                     @RequestParam(name = "sellerTaxId") String sellerTaxId) {
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setPassword(CommonUtils.encode(password));
        userModel.setRole(role);
        userModel.setStatus(1);
        userModel.setCompany(company);
        userModel.setSellerTaxId(sellerTaxId);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getUserByName", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getUserByName(@RequestParam(name = "name") String name) throws BusinessException {
        UserModel userModel = userService.getUserByName(name);
        return CommonReturnType.create(userModel);
    }

    @RequestMapping(value = "/getAllUsers", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType getAllUsers() {
        List<UserModel> userModels = userService.getAllUsers();
        return CommonReturnType.create(userModels);
    }

    @RequestMapping(value = "/updateUser", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType updateUser(@RequestParam(name = "name") String name,
                                       @RequestParam(name = "password") String password,
                                       @RequestParam(name = "role") Integer role,
                                       @RequestParam(name = "status") Integer status,
                                       @RequestParam(name = "company") String company,
                                       @RequestParam(name = "sellerTaxId") String sellerTaxId) {
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setPassword(CommonUtils.encode(password));
        userModel.setRole(role);
        userModel.setStatus(status);
        userModel.setCompany(company);
        userModel.setSellerTaxId(sellerTaxId);
        userService.updateUser(userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "name") String name,
                                  @RequestParam(name = "password") String password) throws BusinessException {
        if (name.isEmpty() || password.isEmpty()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserModel userModel = userService.validateLogin(name, CommonUtils.encode(password));
        // generate token
        String token = jwtTokenUtils.generateToken(userModel.getName(), userModel.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("user", userModel);
        result.put("token", token);
        return CommonReturnType.create(result);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType logout() {
        return CommonReturnType.create("Logout");
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @RequireRole({ROLE_MANAGER})
    public CommonReturnType delete(@RequestParam(name = "name") String name) {
        userService.delete(name);
        return CommonReturnType.create(null);
    }
}
