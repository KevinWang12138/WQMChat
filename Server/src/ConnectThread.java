import java.io.BufferedReader;
import java.io.IOException;

public class ConnectThread implements Runnable{
    /**
     * 负责进行连接的进程
     */
    public BufferedReader socketIn;
    public TotalThread[] totalThreads;
    public int targetport;
    public int selfport;
    public ConnectThread(TotalThread[] totalThreads,BufferedReader socketIn,int selfport){
        this.totalThreads=totalThreads;
        this.socketIn=socketIn;
        this.selfport=selfport;
    }
    @Override
    public void run() {
        while(true){
            try {
                String p=socketIn.readLine();
                if(p==null){
                    continue;
                }
                p=p.substring(5);
                System.out.println(p);
                totalThreads[selfport-4000].timer=0;//重新开始不活跃计时
                if(p.equals("y")){
                    totalThreads[selfport-4000].x="y";
                    return;
                }else if(p.equals("n")){
                    totalThreads[selfport-4000].x="n";
                    continue;
                }else if(p.equals("当前在线")){
                    String nowClients="当前在线:";
                    TotalThread[] t=totalThreads;
                    for(int i=0;i<t.length;i++){
                        if(t[i].succeedConnect&&selfport!=t[i].port){
                            nowClients+=t[i].port;
                            nowClients+=" ";
                        }
                    }
                    totalThreads[selfport-4000].socketOut.println(nowClients);
                    totalThreads[selfport-4000].socketOut.flush();
                    continue;
                }
                targetport=Integer.valueOf(p);
            } catch (IOException e) {
                continue;
            }
            //检查这个端口是否已经连接上，如果没连接上，返回消息，连接失败
            //如果这个端口连接上了，但是正在通信中，那么也不能连接，返回消息，对方正在通信
            //如果这个端口连接上了并且没通信中，那么二者连接
            if(targetport>=4010||targetport<4000){
                totalThreads[selfport-4000].socketOut.println("请选择4000-4009的端口");
                totalThreads[selfport-4000].socketOut.flush();
            }
            if(!totalThreads[targetport-4000].succeedConnect){
                totalThreads[selfport-4000].socketOut.println("该端口无人连接，请重新选择端口");
                totalThreads[selfport-4000].socketOut.flush();
            }else if(totalThreads[targetport-4000].isTalking){
                totalThreads[selfport-4000].socketOut.println("该端口用户正在与他人聊天，请稍后再试");
                totalThreads[selfport-4000].socketOut.flush();
            }else{
                totalThreads[selfport-4000].socketOut.println("正在等待对方应答...");
                totalThreads[selfport-4000].socketOut.flush();
                totalThreads[targetport-4000].socketOut.println("端口号为"+selfport+"的用户请求与您聊天，是否同意？");
                totalThreads[targetport-4000].socketOut.flush();
                while(true){
                    String x=totalThreads[targetport-4000].x;
                    System.out.println(x);
                    if(x.equals("y")){
                        totalThreads[selfport-4000].socketOut.println("成功连接");
                        totalThreads[selfport-4000].socketOut.flush();
                        totalThreads[targetport-4000].socketOut.println("成功连接");
                        totalThreads[targetport-4000].socketOut.flush();


                        totalThreads[selfport-4000].systemIn=totalThreads[targetport-4000].socketIn;
                        totalThreads[targetport-4000].systemIn=totalThreads[selfport-4000].socketIn;


                        totalThreads[targetport-4000].isTalking=true;
                        totalThreads[selfport-4000].isTalking=true;

                        totalThreads[targetport-4000].targetport=selfport;
                        totalThreads[selfport-4000].targetport=targetport;

                        return;
                    }else if(x.equals("n")){
                        totalThreads[selfport-4000].socketOut.println("对方拒绝与您聊天");
                        totalThreads[selfport-4000].socketOut.flush();
                        break;
                    }
                }
            }
        }
    }
}
