package com.wqmchat.client;

import java.io.File;
import java.net.InetAddress;
import java.net.Socket;

public class FileMainThread implements Runnable{
    public int targetport;
    public String path;
    public FileMainThread(String path,int targetport){
        this.targetport=targetport;
        this.path=path;
    }
    @Override
    public void run(){
        System.out.println("系统提示，开始文件传输...");
        try {
            Thread.sleep(200);
            /**
             * 进行文件的传输
             */
            byte ipAddressTemp[] = {117,78,1,7};
            InetAddress ipAddress = InetAddress.getByAddress(ipAddressTemp);
            Socket filesocket = new Socket(ipAddress,targetport);//和服务器进行连接
            FileWriteThread fileWriteThread=new FileWriteThread(path,filesocket);
            Thread t2=new Thread(fileWriteThread);
            t2.start();
            while(true){
                if(!fileWriteThread.over){
                    Thread.sleep(100);
                }else{
                    break;
                }
            }
            System.out.println("系统提示，传输完成");
            filesocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
