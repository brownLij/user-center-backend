package com.ljiaping.usercenter.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljiaping.usercenter.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class RoleFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String base64Header = request.getHeader("X-User-Info");
        if (StringUtils.isNotBlank(base64Header)) {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Header);
            String decodedHeader = new String(decodedBytes, StandardCharsets.UTF_8);
            UserInfo userInfo = new ObjectMapper().readValue(decodedHeader, UserInfo.class);
            log.info("the current user is {} of {} role", userInfo.getAccountName(), userInfo.getRole());
            // Set user info in request attributes for later use
            request.setAttribute("userInfo", userInfo);
        }
        filterChain.doFilter(request, response);
    }
}
