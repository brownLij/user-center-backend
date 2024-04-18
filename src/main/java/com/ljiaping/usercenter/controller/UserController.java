package com.ljiaping.usercenter.controller;

import com.ljiaping.usercenter.common.BaseResponse;
import com.ljiaping.usercenter.common.ResultUtils;
import com.ljiaping.usercenter.model.AccessInfo;
import com.ljiaping.usercenter.model.UserInfo;
import com.ljiaping.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{resource}")
    public BaseResponse<Boolean> checkUserAccess(@PathVariable String resource, HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        long userId = userInfo.getUserId();
        AccessInfo accessInfo = userService.findAccessInfoById(userId);
        boolean isAccess = Objects.nonNull(accessInfo) && accessInfo.getEndpoints().contains(resource);
        return ResultUtils.success(isAccess);
    }
}
