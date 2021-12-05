package com.wqmchat.client;

import android.os.Bundle;
import android.os.Message;

import com.wqmchat.ui.dashboard.DashboardFragment;
import com.wqmchat.ui.home.HomeFragment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReadThread implements Runnable{
    private ServerSocket server;
    private Socket socket;
    private DataInputStream dis;
    private FileOutputStream fos;
    public boolean over;
    public FileReadThread(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            // 文件名和长度
            String fileName = dis.readUTF();
            long fileLength = dis.readLong();
            File directory = new File("/storage/emulated/0/WQMChat");
            if(!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);

            fos = new FileOutputStream(file);
            System.out.println("file。。。。。。。。。。。。。。"+file);
            System.out.println("fileName。。。。。。。。。。。。。。"+fileName);

            System.out.println("======== 开始接收文件 ========");
            byte[] bytes = new byte[1024];
            int length = 0;
            while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }

            System.out.println("======== 文件接收成功 ========");




            over=true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
                if(dis != null)
                    dis.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

