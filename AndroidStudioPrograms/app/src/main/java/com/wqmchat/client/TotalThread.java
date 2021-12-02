package com.wqmchat.client;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.wqmchat.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class TotalThread implements Runnable{
    public int port;
    public byte ipAddressTemp[] = {117,78,1,7};
    public InetAddress ipAddress;
    public Socket socket;
    public BufferedReader socketIn;//客户端传到本地的流
    public ClientReadThread clientReadThread;
    public TextView tv;
    public ListView lv;
    public Context c;
    public MainActivity mainActivity;
    public TotalThread(int port, TextView tv, ListView lv, Context c, MainActivity mainActivity){
        this.port=port;
        this.tv=tv;
        this.lv=lv;
        this.c=c;
        this.reset=false;
        this.mainActivity=mainActivity;
    }
    public boolean reset;
    @Override
    public void run() {
        try {
            ipAddress = InetAddress.getByAddress(ipAddressTemp);
            socket = new Socket(ipAddress, port);//和服务器进行连接
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));//客户端传到本地的流
            clientReadThread=new ClientReadThread(socketIn,this,tv,lv,c,mainActivity);
            Thread read=new Thread(clientReadThread);
            read.start();
            while(!reset){}
            socket.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
