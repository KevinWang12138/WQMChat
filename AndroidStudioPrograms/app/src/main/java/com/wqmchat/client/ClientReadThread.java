package com.wqmchat.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import com.wqmchat.MainActivity;
import com.wqmchat.ui.dashboard.DashboardFragment;
import com.wqmchat.ui.home.HomeFragment;
import java.io.BufferedReader;
import java.io.IOException;


public class ClientReadThread implements Runnable{
    public String inTemp = null;
    public BufferedReader socketIn;
    public TotalThread myselfTotalThread;
    public TextView tv;
    public ListView lv;
    public Context c;
    public MainActivity mainActivity;
    public ClientReadThread(BufferedReader socketIn, TotalThread myselfTotalThread, TextView tv,ListView lv, Context c,MainActivity mainActivity){
        this.socketIn=socketIn;
        this.myselfTotalThread=myselfTotalThread;
        this.tv=tv;
        this.lv=lv;
        this.c=c;
        this.mainActivity=mainActivity;
    }
    @Override
    public void run() {
        while(1!= 2){
            try {
                inTemp = socketIn.readLine();
                if(inTemp==null){
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle data = new Bundle();
                    data.putString("bye", "对方已下线");
                    msg.setData(data);
                    HomeFragment.handlerSuccess.sendMessage(msg);
                    myselfTotalThread.reset=true;
                    socketIn.close();


                    msg = new Message();
                    msg.what = 0;
                    data = new Bundle();
                    data.putString("bye", "清理聊天框");
                    msg.setData(data);
                    HomeFragment.handlerSuccess.sendMessage(msg);
                    break;
                }
                switch (inTemp){
                    case "请选择4000-4009的端口":
                        Message msg = new Message();
                        msg.what = 1;
                        //这三句可以传递数据
                        Bundle data = new Bundle();
                        data.putString("message", inTemp);//COUNT是标签,handleMessage中使用
                        msg.setData(data);
                        DashboardFragment.handlerSuccess.sendMessage(msg); //向Handler发送消息,更新UI
                        continue;
                    case "该端口用户正在与他人聊天，请稍后再试":
                        msg = new Message();
                        msg.what = 1;
                        data = new Bundle();
                        data.putString("message", inTemp);
                        msg.setData(data);
                        DashboardFragment.handlerSuccess.sendMessage(msg);
                        continue;
                    case "正在等待对方应答...":
                        msg = new Message();
                        msg.what = 1;
                        data = new Bundle();
                        data.putString("message", inTemp);
                        msg.setData(data);
                        DashboardFragment.handlerSuccess.sendMessage(msg);
                        continue;
                    case "对方拒绝与您聊天":
                        msg = new Message();
                        msg.what = 1;
                        data = new Bundle();
                        data.putString("message", inTemp);
                        msg.setData(data);
                        DashboardFragment.handlerSuccess.sendMessage(msg);
                        continue;
                    case "成功连接":
                        msg = new Message();
                        msg.what = 1;
                        data = new Bundle();
                        data.putString("message", inTemp);
                        msg.setData(data);
                        DashboardFragment.handlerSuccess.sendMessage(msg);
                        mainActivity.HasChosenTargetClient=true;
                        continue;
                    default:
                        if(inTemp.length()!=23){
                            break;
                        }
                        if(inTemp.substring(0,4).equals("端口号为")&&inTemp.substring(8).equals("的用户请求与您聊天，是否同意？")){
                            msg = new Message();
                            msg.what = 1;
                            data = new Bundle();
                            data.putString("message", inTemp);
                            msg.setData(data);
                            DashboardFragment.handlerSuccess.sendMessage(msg);
                            mainActivity.HasChosenTargetClient=true;
                            continue;
                        }
                }
                if(inTemp.length()>5){
                    if(inTemp.substring(0,5).equals("当前在线:")){
                        //为当前在线界面设计界面展示当前在线人数
                        String nowClients=inTemp.substring(5);
                        int totalnowClients=nowClients.length()/5;//统计在线人数
                        //记录在线人端口号
                        String[] ports=new String[totalnowClients];
                        //进行展示，放在listView里面
                        for(int i=0;i<totalnowClients;i++){
                            ports[i]=nowClients.substring(i*5,i*5+4);
                        }
                        DashboardFragment.viewUI(ports,c);
                        continue;
                    }
                }
                HomeFragment.viewUI(inTemp);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}