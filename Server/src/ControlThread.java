import java.io.BufferedReader;

public class ControlThread implements Runnable{
    /**
     * 监视进程，监控十个端口的状况，如果某个端口连接后又断开，需要重新设置他。
     */
    public TotalThread[] totalThreads;
    public Thread[] threads;
    public ControlThread(TotalThread[] totalThreads, Thread[] threads){
        this.totalThreads=totalThreads;
        this.threads=threads;
    }
    @Override
    public void run() {
        while(true){
            for(int i=0;i<10;i++){
                if(totalThreads[i].writeThread!=null&&totalThreads[i].writeThread.byeClient){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalThreads[i]=new TotalThread(i+4000,totalThreads);
                    threads[i]=new Thread(totalThreads[i]);
                    threads[i].start();
                }
            }
        }
    }
}
