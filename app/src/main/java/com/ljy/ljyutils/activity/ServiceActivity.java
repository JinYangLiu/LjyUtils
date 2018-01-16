package com.ljy.ljyutils.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.service.MyForegroundNormalService;
import com.ljy.ljyutils.service.MyNormalService;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private Context mContext = this;
    private ServiceConnection serviceConnection;
    private MyNormalService.MyBinder mBinder;
    private boolean isServiceBind;
    private List<Intent> mIntentList = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "123321") {
                String info = intent.getStringExtra("info");
                LjyToastUtil.toast(mContext, "info:" + info);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LjyLogUtil.i("onServiceConnected");
                if (service != null) {
                    mBinder = (MyNormalService.MyBinder) service;
                    mBinder.doSomething();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LjyLogUtil.i("onServiceDisconnected");
            }
        };
    }

    public void onServiceClick(View view) {
//        Intent intent = new Intent(mContext, MyNormalService.class);
        Intent intent = new Intent(mContext, MyForegroundNormalService.class);
        switch (view.getId()) {
            case R.id.btn_start:
                startService(intent);
                //若第一次：---->onCreate---->onStartCommand
                //若多次：---->onStartCommand，除非调用stopService
                break;
            case R.id.btn_stop:
                stopService(intent);
                //---->onDestroy
                //多次调用stopService无效
                break;
            case R.id.btn_bind:
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                isServiceBind = true;
                //---->onCreate ---->onBind ---->ServiceConnection.onServiceConnected ---->mBinder.doSomething()：doSomething....
                //多次调用无效
                break;
            case R.id.btn_unbind:
                if (isServiceBind) {
                    unbindService(serviceConnection);
                    isServiceBind = false;
                }

                //---->onUnbind ---->onDestroy
                //多次调用会报错： java.lang.IllegalArgumentException: Service not registered
                break;
            case R.id.btn_startIntentService:
                IntentFilter filter = new IntentFilter();
                filter.addAction("123321");
                registerReceiver(mReceiver, filter);
                for (int i = 0; i < 4; i++) {
                    Intent intentTask = new Intent("com.ljy.ljyutils.mIntentService");
                    Bundle bundle = new Bundle();
                    bundle.putString("taskName", "task_" + i);
                    intentTask.putExtras(bundle);
                    startService(intentTask);
                    mIntentList.add(intentTask);
                }
                //  不用调用stopService(intent)，当所有任务完成后会自动销毁
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isServiceBind) {
            unbindService(serviceConnection);
            isServiceBind = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
