package org.cityu.controller;

import org.cityu.common.annotation.MerchantOnly;
import org.cityu.dataobject.UserDO;
import org.cityu.service.MerchantService;
import org.cityu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@MerchantOnly
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;
}
