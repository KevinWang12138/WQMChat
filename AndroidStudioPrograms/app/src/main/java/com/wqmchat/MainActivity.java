package com.wqmchat;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wqmchat.client.MainThread;
import com.wqmchat.databinding.ActivityMainBinding;
import com.wqmchat.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public MainThread mt;
    public int port;
    public TextView tv;
    public ListView lv;
    public Context c;
    public boolean HasChosenTargetClient;//用来标识是否已经选中了聊天对象，如果没有选中，不许交流
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //在MainActivity创建的时候就开始创建
        port=4000;
        TextView tv=getLayoutInflater().inflate(R.layout.fragment_home, null).findViewById(R.id.textView);
        ListView lv=getLayoutInflater().inflate(R.layout.fragment_dashboard, null).findViewById(R.id.list1);
        Context c=getLayoutInflater().inflate(R.layout.fragment_dashboard, null).getContext();
        mt=new MainThread(port,tv,lv,c,this);
        Thread t=new Thread(mt);
        t.start();

    }

}