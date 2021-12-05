import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TotalThread implements Runnable{
    public int port;
    public int targetport;
    public ServerSocket serverSocket;
    public Socket socket;
    public BufferedReader socketIn;
    public ServerWriteThread writeThread;
    public BufferedReader systemIn;
    public PrintWriter socketOut;
    public boolean succeedConnect;//标志客户端成功连接上服务器
    public ConnectThread connectThread;
    public TotalThread[] totalThreads;
    public boolean isTalking;
    public String x="";
    public int timer=0;
    public TotalThread(int port,TotalThread[] totalThreads){
        this.totalThreads=totalThreads;
        this.port=port;
    }
    public boolean active;
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            succeedConnect=true;//此时已经成功连接
            System.out.println("欢迎来自"+ socket.getPort()+"的同学");
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintWriter(socket.getOutputStream());
            connectThread=new ConnectThread(totalThreads,socketIn,port);
            Thread t=new Thread(connectThread);
            t.start();
            for(timer=0;timer<60;timer++){
                if(systemIn==null){
                    Thread.sleep(1000);
                }else{
                    active=true;
                    break;
                }
            }
            if(!active){//如果发现不活跃（60秒内无任何操作），直接使其强制退出，让出端口号
                writeThread=new ServerWriteThread(systemIn,socketOut,this);
                writeThread.byeClient=true;
                socket.close();
                serverSocket.close();
                socketIn.close();
                socketOut.close();
            }else{
                writeThread=new ServerWriteThread(systemIn,socketOut,this);
                Thread write=new Thread(writeThread);
                write.start();
                while(true){
                    Thread.sleep(100);
                    if(writeThread.byeClient){
                        serverSocket.close();
                        socketIn.close();
                        socket.close();
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
