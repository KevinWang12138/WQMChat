package com.wqmchat.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wqmchat.MainActivity;
import com.wqmchat.R;
import com.wqmchat.client.ClientWriteThread;
import com.wqmchat.client.MainThread;
import com.wqmchat.databinding.FragmentDashboardBinding;
import com.wqmchat.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.io.PrintWriter;

public class HomeFragment extends Fragment {


    private static FragmentHomeBinding binding;
    public static Handler handlerSuccess;
    public static void viewUI(String x){
        binding.textView.post(new Runnable() {
            @Override
            public void run() {
                String add=binding.textView.getText().toString()+"\n";
                binding.textView.setText(add+x);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MainActivity mainActivity=(MainActivity) getActivity();
        MainThread mt=mainActivity.mt;
        binding.button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.HasChosenTargetClient){//只有当选中了聊天对象后，才允许进行聊天
                    String inputText=binding.inputText.getText().toString();
                    String add=binding.textView.getText().toString()+"\n";
                    binding.textView.setText(add+inputText);
                    binding.inputText.setText("");
                    ClientWriteThread cwt=new ClientWriteThread(mt.totalThread.socket,inputText);
                    Thread t=new Thread(cwt);
                    t.start();
                }else{//设置一个dialog，提示必须先在当前在线界面选择聊天对象才能聊天。
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                    dialog.setTitle("请先在当前在线界面选择一位用户进行连接");
                    dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }

            }
        } );




        handlerSuccess = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                String title=msg.getData().getString("bye");
                if(title.equals("对方已下线")){
                    dialog.setTitle(title);
                    dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }else if(title.equals("对方向您传输了一个文件")){
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