package com.ljiaping.usercenter.controller;

import com.ljiaping.usercenter.common.BaseResponse;
import com.ljiaping.usercenter.common.ErrorCode;
import com.ljiaping.usercenter.common.ResultUtils;
import com.ljiaping.usercenter.exception.BusinessException;
import com.ljiaping.usercenter.model.AccessInfo;
import com.ljiaping.usercenter.model.UserInfo;
import com.ljiaping.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public BaseResponse<AccessInfo> addUserAccess(@RequestBody AccessInfo accessInfo, HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        if (!"admin".equals(userInfo.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Need admin role");
        }
        //simple validation
        if (Objects.isNull(accessInfo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.upsertAccessInfo(accessInfo);
        return ResultUtils.success(accessInfo);
    }
}