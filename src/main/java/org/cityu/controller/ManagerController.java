package org.cityu.controller;

import org.cityu.common.annotation.ManagerOnly;
import org.cityu.response.CommonReturnType;
import org.cityu.service.UserService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.cityu.controller.BaseController.CONTENT_TYPE_FORMED;

@RestController
@RequestMapping("/manager")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@ManagerOnly
public class ManagerController {

    @Autowired
    private UserService userService;

    /**
     * Create account
     * @param name
     * @param password
     * @param role
     * @return
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "role") Integer role,
                                     @RequestParam(name = "sellerTaxId") String sellerTaxId) {
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setPassword(password);
        userModel.setRole(role);
        userModel.setAvailable(1);
        userModel.setSellerTaxId(sellerTaxId);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * Get User Information
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUser", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) {
        UserModel userModel = userService.getUserById(id);
        return CommonReturnType.create(userModel);
    }

    /**
     * Change account information
     * @param id
     * @return
     */
    @RequestMapping(value = "/changeUserInfo", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType changeUserInfo(@RequestParam(name = "id") Integer id,
                                           @RequestParam(name = "name") String name,
                                           @RequestParam(name = "password") String password,
                                           @RequestParam(name = "role") Integer role,
                                           @RequestParam(name = "available") Integer available) {
        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setName(name);
        userModel.setPassword(password);
        userModel.setRole(role);
        userModel.setAvailable(available);
        userService.changeUserInfo(userModel);
        return CommonReturnType.create(null);
    }
}
