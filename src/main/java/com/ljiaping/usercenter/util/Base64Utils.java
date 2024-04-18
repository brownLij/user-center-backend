package com.ljiaping.usercenter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljiaping.usercenter.model.UserInfo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    public static void main(String[] args) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setRole("admin");
        userInfo.setAccountName("Admin");
        userInfo.setUserId(123456);
        String s = encodeBase64(userInfo);
        System.out.println(s);
    }

    public static String encodeBase64(UserInfo userInfo) throws Exception {
        String json = new ObjectMapper().writeValueAsString(userInfo);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    public static UserInfo decodeBase64(String encodedString) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String json = new String(decodedBytes, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(json, UserInfo.class);
    }
}
