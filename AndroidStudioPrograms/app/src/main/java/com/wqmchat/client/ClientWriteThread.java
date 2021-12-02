package com.wqmchat.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWriteThread implements Runnable{
    String readline = null;
    public String inTemp = null;
    public Socket socket;
    public ClientWriteThread(Socket socket,String inTemp){
        this.socket=socket;
        this.inTemp=inTemp;
    }
    @Override
    public void run() {
        PrintWriter socketOut = null;//将本地文字输出到服务器的流
        try {
            socketOut = new PrintWriter(socket.getOutputStream());
            socketOut.println(socket.getPort()+":"+inTemp);
            socketOut.flush();
            //socketOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}