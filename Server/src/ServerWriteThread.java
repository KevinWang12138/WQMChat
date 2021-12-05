import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerWriteThread implements Runnable{
    public BufferedReader systemIn;
    public PrintWriter socketOut;
    public TotalThread totalThread;
    public boolean byeClient;//标志这个端口的客户端已经断开连接，我们再见了！
    public ServerWriteThread(BufferedReader systemIn,PrintWriter socketOut,TotalThread totalThread){
        this.systemIn=systemIn;
        this.socketOut=socketOut;
        this.totalThread=totalThread;
    }
    @Override
    public void run() {
        String readline = "";
        while (true) {
            try {
                readline = systemIn.readLine();
                if(readline.substring(5).equals("bye")){
                    break;
                }else if(readline.substring(5).equals("当前在线")){
                    String nowClients="";
                    TotalThread[] t=totalThread.totalThreads;
                    for(int i=0;i<t.length;i++){
                        if(t[i].succeedConnect&&totalThread.port!=t[i].port){
                            nowClients+=t[i].port;
                            nowClients+=" ";
                        }
                    }
                    socketOut.println(nowClients);
                    socketOut.flush();
                    continue;
                }else if(readline.substring(5).equals("开始传输文件")){
                    socketOut.println(readline);
                    socketOut.flush();
                    int targetport=totalThread.targetport;
                    ServerSocket inserverSocket = new ServerSocket(totalThread.port+10);
                    Socket in = inserverSocket.accept();
                    ServerSocket outserverSocket = new ServerSocket(totalThread.totalThreads[targetport-4000].port+10);
                    Socket out = outserverSocket.accept();
                    FileThread fileThread=new FileThread(in,out);
                    Thread t=new Thread(fileThread);
                    t.start();
                    while(true){
                        if(!fileThread.over){
                            Thread.sleep(100);
                        }else{
                            break;
                        }
                    }
                    inserverSocket.close();
                    outserverSocket.close();
                    continue;
                }
                socketOut.println(readline);
                socketOut.flush();//赶快刷新使Client收到，也可以换成socketOut.println(readline, ture)
            } catch (IOException | InterruptedException e) {
                break;
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socketOut.close();
        byeClient=true;
    }
}
