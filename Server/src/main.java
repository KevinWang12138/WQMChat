import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class main {
    public static void main(String[] args) throws Exception {
        //创建10个端口，来人了就连上
        //后续修改，释放端口
        TotalThread[] totalThreads=new TotalThread[10];
        Thread[] threads=new Thread[10];
        //BufferedReader[] readers=new BufferedReader[10];
        System.out.println("HELLO");
        for(int i=0;i<10;i++){
            totalThreads[i]=new TotalThread(i+4000,totalThreads);
            threads[i]=new Thread(totalThreads[i]);
            threads[i].start();
        }
        Thread controlThread=new Thread(new ControlThread(totalThreads,threads));//监视进程
        controlThread.start();
    }
}
