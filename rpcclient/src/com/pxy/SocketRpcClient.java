package com.pxy;

import com.pxy.proxy.ServiceProxy;
import com.pxy.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class SocketRpcClient {
    private String interfaceName;

    public SocketRpcClient(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void run() {
        System.out.println("rpc client start on 8001!!!");
        Class interfaceType = null;
        try {
            interfaceType = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        InvocationHandler handler = new ServiceProxy<>(interfaceType);
        UserService userService = (UserService) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler);
        Object object = userService.getUserInfo(1);
        System.out.println(object);
    }

    public static void main(String[] args) {
        SocketRpcClient socketRpcClient = new SocketRpcClient("com.pxy.service.UserService");
        socketRpcClient.run();
    }
}
