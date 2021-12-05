import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileThread implements Runnable{
    public boolean over;
    public Socket in;
    public Socket out;
    private ServerSocket server;
    private DataInputStream dis;
    private DataOutputStream dos;
    public FileThread(Socket in,Socket out){
        this.in=in;
        this.out=out;
    }
    @Override
    public void run() {
        //先接收传入方的流，再把传入方的流传给接收方
        try {
            dis = new DataInputStream(out.getInputStream());//获得传入方的输入流
            dos = new DataOutputStream(in.getOutputStream());//获得传出方的输出流
            // 从传入方得到文件名和长度
            String fileName = dis.readUTF();
            long fileLength = dis.readLong();
            dos.writeUTF(fileName);
            dos.flush();
            dos.writeLong(fileLength);
            dos.flush();
            byte[] bytes = new byte[1024];
            int length = 0;
            while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            over=true;
            /**
             * 传输结束
             */
            dis.close();
            dos.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
