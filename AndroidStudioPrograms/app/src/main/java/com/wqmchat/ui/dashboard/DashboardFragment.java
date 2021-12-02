package com.wqmchat.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wqmchat.MainActivity;
import com.wqmchat.R;
import com.wqmchat.client.ClientWriteThread;
import com.wqmchat.client.MainThread;
import com.wqmchat.databinding.FragmentDashboardBinding;
import com.wqmchat.databinding.FragmentHomeBinding;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;

public class DashboardFragment extends Fragment {

    private static FragmentDashboardBinding binding;
    private static String[] Ports;
    public static Handler handlerSuccess;
    public static void viewUI(String[] ports, Context c){
        Ports=ports;
        binding.list1.post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1,ports);
                binding.list1.setAdapter(adapter);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //每次都发送一次请求，请求更新在线人数
        MainActivity mainActivity=(MainActivity) getActivity();
        ClientWriteThread cwt1=new ClientWriteThread(mainActivity.mt.totalThread.socket,"当前在线");
        Thread t1=new Thread(cwt1);
        t1.start();

        /**
         * 点击当前在线列表中的某一项后，跳出一个对话框，询问是否和他连接
         * 进行选择后，如果是，就向服务器发送消息
         * 如果不是，退出即可
         */
        binding.list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientWriteThread cwt2=new ClientWriteThread(mainActivity.mt.totalThread.socket,Ports[position]);
                Thread t2=new Thread(cwt2);
                t2.start();
            }
        });
        handlerSuccess = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                String title=msg.getData().getString("message");
                if(title.length()==23){
                    dialog.setTitle(title);
                    dialog.setPositiveButton("否",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClientWriteThread cwt2=new ClientWriteThread(mainActivity.mt.totalThread.socket,"n");
                            Thread t2=new Thread(cwt2);
                            t2.start();
                        }
                    });
                    dialog.setNegativeButton("是",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClientWriteThread cwt2=new ClientWriteThread(mainActivity.mt.totalThread.socket,"y");
                            Thread t2=new Thread(cwt2);
                            t2.start();
                        }
                    });
                    dialog.show();
                }else{
                    dialog.setTitle(title);
                    dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
            }
        };
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}