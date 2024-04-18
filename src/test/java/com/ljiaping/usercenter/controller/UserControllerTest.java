package com.ljiaping.usercenter.controller;


import com.ljiaping.usercenter.common.BaseResponse;
import com.ljiaping.usercenter.model.AccessInfo;
import com.ljiaping.usercenter.model.UserInfo;
import com.ljiaping.usercenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest request;
    private UserInfo userInfo;
    private AccessInfo accessInfo;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        userInfo = new UserInfo();
        userInfo.setUserId(1L);
        request.setAttribute("userInfo", userInfo);

        accessInfo = new AccessInfo(1, List.of("A", "B"));
    }

    @Test
    public void testCheckUserAccess_WithAccess() {
        when(userService.findAccessInfoById(userInfo.getUserId())).thenReturn(accessInfo);
        BaseResponse<Boolean> response = userController.checkUserAccess("A", request);
        assertTrue(response.getData());
        verify(userService, times(1)).findAccessInfoById(userInfo.getUserId());
    }

    @Test
    public void testCheckUserAccess_WithoutAccess() {
        when(userService.findAccessInfoById(userInfo.getUserId())).thenReturn(accessInfo);
        BaseResponse<Boolean> response = userController.checkUserAccess("C", request);
        assertFalse(response.getData());
        verify(userService, times(1)).findAccessInfoById(userInfo.getUserId());
    }
}
