package com.ljiaping.usercenter.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljiaping.usercenter.model.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleFilterTest {

    @Mock
    private FilterChain filterChain;

    private RoleFilter roleFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        roleFilter = new RoleFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDoFilterInternalWithUserInfoHeader() throws Exception {
        // Prepare a base64 encoded user info header
        UserInfo userInfo = new UserInfo();
        userInfo.setAccountName("testUser");
        userInfo.setRole("user");
        String userInfoJson = objectMapper.writeValueAsString(userInfo);
        String base64UserInfo = java.util.Base64.getEncoder().encodeToString(userInfoJson.getBytes());
        request.addHeader("X-User-Info", base64UserInfo);

        roleFilter.doFilterInternal(request, response, filterChain);

        UserInfo resultUserInfo = (UserInfo) request.getAttribute("userInfo");
        assertEquals("testUser", resultUserInfo.getAccountName());
        assertEquals("user", resultUserInfo.getRole());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
