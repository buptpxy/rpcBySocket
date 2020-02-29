package com.pxy;

import com.alibaba.fastjson.JSON;
import com.pxy.rpc.*;
import com.pxy.utils.RpcUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketRpcServer {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 8002;
    private static InetSocketAddress SOCKET_ADDRESS = new InetSocketAddress(IP,PORT);

    public void run() {
        ServerSocket server = createSocketServer();
        System.out.println("rpc server start on 8002!!!");
        ObjectInputStream input =null;
        ObjectOutputStream output =null;
        Socket socket=null;
        try {
            while(true){
                socket = server.accept();
                input =new ObjectInputStream(socket.getInputStream());
                RpcParam rpcParam = (RpcParam) input.readObject(); //注意此处如果rpcclient的RpcRaram和rpcserver的RpcParam的全限定名不一样则会报错。

                Result result = RpcUtil.getServerResult(rpcParam);
                output = new ObjectOutputStream(socket.getOutputStream());
//                output.writeObject(JSON.toJSONString(result)); //这里使用output.writeUTF会使rpcclient接收不到发过去的字符串
                output.writeObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnect(output,input,socket);
        }
    }

    private ServerSocket createSocketServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(SOCKET_ADDRESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
    }

    private void closeConnect(ObjectOutputStream output, ObjectInputStream input, Socket socket) {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null) socket.close();
            System.out.println("server 关闭 socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketRpcServer().run();
    }

}