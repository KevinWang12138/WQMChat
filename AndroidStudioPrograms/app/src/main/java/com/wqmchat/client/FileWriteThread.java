package com.wqmchat.client;
import java.io.*;
import java.net.Socket;

public class FileWriteThread implements Runnable{
    public String url;
    private Socket client;
    private FileInputStream fis;
    private DataOutputStream dos;
    public boolean over;
    public FileWriteThread(String url,Socket client){
        this.url=url;
        this.client=client;
    }
    @Override
    public void run() {
        File file=new File(url);
        try {
            fis = new FileInputStream(file);
            //BufferedInputStream bi=new BufferedInputStream(new InputStreamReader(new FileInputStream(file),"GBK"));
            dos = new DataOutputStream(client.getOutputStream());//client.getOutputStream()返回此套接字的输出流
            Thread.sleep(1000);
            //文件名、大小等属性
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
            // 开始传输文件
            System.out.println("======== 开始传输文件 ========");
            byte[] bytes = new byte[1024];
            int length = 0;

            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            System.out.println("======== 文件传输成功 ========");
            over=true;
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
            System.out.println("客户端文件传输异常");
        }finally{
            try {
                fis.close();
                dos.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}