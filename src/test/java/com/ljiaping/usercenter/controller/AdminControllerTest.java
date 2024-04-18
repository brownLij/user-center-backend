package com.ljiaping.usercenter.controller;

import com.ljiaping.usercenter.common.BaseResponse;
import com.ljiaping.usercenter.common.ErrorCode;
import com.ljiaping.usercenter.exception.BusinessException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    private MockHttpServletRequest request;
    private UserInfo adminUserInfo;
    private AccessInfo accessInfo;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        adminUserInfo = new UserInfo();
        adminUserInfo.setRole("admin");
        accessInfo = new AccessInfo(1, List.of("A"));
    }

    @Test
    void testAddUserAccessWithAdminRole() {
        request.setAttribute("userInfo", adminUserInfo);

        BaseResponse<AccessInfo> response = adminController.addUserAccess(accessInfo, request);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals(accessInfo, response.getData());
        verify(userService, times(1)).upsertAccessInfo(accessInfo);
    }

    @Test
    void testAddUserAccessWithoutAdminRole() {
        UserInfo nonAdminUserInfo = new UserInfo();
        nonAdminUserInfo.setRole("user");
        request.setAttribute("userInfo", nonAdminUserInfo);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.addUserAccess(accessInfo, request);
        });

        assertEquals(ErrorCode.NO_AUTH.getCode(), exception.getCode());
        assertEquals(ErrorCode.NO_AUTH.getMessage(), exception.getMessage());
        verify(userService, never()).upsertAccessInfo(any(AccessInfo.class));
    }
}
