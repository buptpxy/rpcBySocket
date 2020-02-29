package com.pxy.utils;

import com.alibaba.fastjson.JSON;
import com.pxy.rpc.Result;
import com.pxy.rpc.RpcParam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketUtil {
    private static String IP = "127.0.0.1";
    private static int PORT = 8002;
    private static InetSocketAddress SOCKET_ADDRESS = new InetSocketAddress(IP, PORT);
    public static Result callRemoteService(RpcParam rpcParam) {
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        Result result = null;
        try {
            socket = new Socket();
            socket.connect(SOCKET_ADDRESS);

            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(rpcParam);

            input = new ObjectInputStream(socket.getInputStream());

            Object resultObj = input.readObject(); //这里使用input.readUTF()会接收不到rpcclient发过来的字符串
            result = (Result)resultObj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            result = Result.getFailResult("调用远程方法失败");
        } finally {
            closeConnect(socket, output, input);
        }
        return result;
    }

    //关闭连接
    private static void closeConnect(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        try {
            if (socket != null) socket.close();
            if (output != null) output.close();
            if (input != null) input.close();
            System.out.println("client关闭socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
