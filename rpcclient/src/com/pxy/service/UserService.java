package com.pxy.service;

import com.pxy.proxy.RemoteClass;

@RemoteClass("com.pxy.service.UserServiceImpl")
public interface UserService {
    String getUserInfo(int id);
}
