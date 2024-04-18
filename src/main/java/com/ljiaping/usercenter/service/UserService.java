package com.ljiaping.usercenter.service;

import com.ljiaping.usercenter.model.AccessInfo;

public interface UserService {

    void upsertAccessInfo(AccessInfo accessInfo);

    AccessInfo findAccessInfoById(long id);
}
