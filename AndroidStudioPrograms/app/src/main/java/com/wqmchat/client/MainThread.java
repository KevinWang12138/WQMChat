package com.wqmchat.client;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.wqmchat.MainActivity;

public class MainThread implements Runnable{
    public int port;
    public TotalThread totalThread;
    public TextView tv;
    public ListView lv;
    public Context c;
    public MainActivity mainActivity;
    public MainThread(int port, TextView tv, ListView lv, Context c, MainActivity mainActivity){
        this.port=port;
        this.tv=tv;
        this.lv=lv;
        this.c=c;
        this.mainActivity=mainActivity;
    }
    @Override
    public void run(){
        Thread t1;
        totalThread=new TotalThread(port,tv,lv,c,mainActivity);
        t1=new Thread(totalThread);
        t1.start();
        while(true){
            if(totalThread.reset){
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("重启");
                totalThread=new TotalThread(port,tv,lv,c,mainActivity);
                t1=new Thread(totalThread);
                t1.start();
                mainActivity.HasChosenTargetClient=false;//重新设置，现在还没有连接到客户。

            }else{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}