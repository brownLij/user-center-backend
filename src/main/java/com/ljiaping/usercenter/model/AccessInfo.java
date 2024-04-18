package com.ljiaping.usercenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessInfo {
    private long userId;
    private List<String> endpoints;
}
