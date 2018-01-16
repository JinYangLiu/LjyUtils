package com.ljy.ljyutils.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.broadcast.MyBroadcastReceiver4;
import com.ljy.ljyutils.broadcast.MyBroadcastReceiver5;
import com.ljy.util.LjyLogUtil;

public class BroadcastActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

    }

    public void onBroadcastClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ACTION_VIEW:
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:10086"));//拨号程序
//                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://mail.google.com"));//浏览器(网址必须带http)
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:39.899533,116.036476"));//打开地图定位
                startActivity(intent);
                break;
            case R.id.btn_sendBroadcast:
                //发送普通广播
                Intent intent2 = new Intent();
                intent2.setAction("com.ljy.ljyutils.broadcast");
                intent2.putExtra("info", "我是广播传送的消息啊");
                intent2.setPackage("com.ljy.ljyutils");//设置package可以防止其他应用接受到本条广播
//                BroadcastActivity.this.sendBroadcast(intent2);   //普通广播发送
                BroadcastActivity.this.sendOrderedBroadcast(intent2, null);   //有序广播发送
                localBroadcastManager.sendBroadcast(intent2);
                LjyLogUtil.i("发送了广播");
                break;
        }
    }

    /**
     * App应用内广播（Local Broadcast）:
     * <p>
     * 1.Android中的广播可以跨App直接通信（exported对于有intent-filter情况下默认值为true）
     * 2.可能出现的问题：
     * (1)其他App针对性发出与当前App intent-filter相匹配的广播，由此导致当前App不断接收广播并处理；
     * (2)其他App注册与当前App一致的intent-filter用于接收广播，获取广播具体信息；即会出现安全性 & 效率性的问题。
     * 3.使用App应用内广播:
     * 具体使用1 - 将全局广播设置成局部广播
     * (1)注册广播时将exported属性设置为false，使得非本App内部发出的此广播不被接收；
     * (2)在广播发送和接收时，增设相应权限permission，用于权限验证；
     * (3)发送广播时指定该广播接收器所在的包名，此广播将只会发送到此包中的App内与之相匹配的有效广播接收器中。通过intent.setPackage(packageName)指定报名
     * 具体使用2 - 使用封装好的LocalBroadcastManager类
     */

    @Override
    protected void onResume() {
        super.onResume();
        //在Manifest中是静态注册
        //      1.常驻，不受任何组件生命周期影响，
        //      2.应用程序关闭后如果有广播来，依然会接受到，继而执行onReceive中的代码
        //      3.缺点：耗电，占内存

        //下面的是动态注册
        //      1.非常驻，跟随组件生命周期变化
        //      2.组件结束=广播接收器结束，需要记得unregisterReceiver
        //Priority属性相同者，动态注册的广播优先；

        // 1. 实例化BroadcastReceiver子类 &  IntentFilter
//        broadcastReceiver = new MyBroadcastReceiver4();
        broadcastReceiver = new MyBroadcastReceiver5();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(999);

        // 2. 设置接收广播的类型

        //系统广播（System Broadcast）
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        // 3. 动态注册：调用Context的registerReceiver（）方法
//        registerReceiver(broadcastReceiver, intentFilter);

        //注册应用内广播：只能收到localBroadcastManager.sendBroadcast发送的广播
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.ljy.ljyutils.broadcast");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastReceiver2= new MyBroadcastReceiver4();
        localBroadcastManager.registerReceiver(broadcastReceiver2, intentFilter2);
    }

    //在onResume()注册、onPause()注销是因为onPause()在App死亡前一定会被执行，
    // 从而保证广播在App死亡前一定会被注销，从而防止内存泄露。
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(broadcastReceiver);
//        localBroadcastManager.unregisterReceiver(broadcastReceiver2);
    }

    /**
     *
     监听网络变化	            android.net.conn.CONNECTIVITY_CHANGE
     关闭或打开飞行模式	    Intent.ACTION_AIRPLANE_MODE_CHANGED
     充电时或电量发生变化	    Intent.ACTION_BATTERY_CHANGED
     电池电量低	            Intent.ACTION_BATTERY_LOW
     电池电量充足         	Intent.ACTION_BATTERY_OKAY（即从电量低变化到饱满时会发出广播)
     系统启动完成后          Intent.ACTION_BOOT_COMPLETED(仅广播一次)
     按下照相时的拍照按键时	Intent.ACTION_CAMERA_BUTTON(硬件按键)
     屏幕锁屏	            Intent.ACTION_CLOSE_SYSTEM_DIALOGS
     设备当前设置被改变时	    Intent.ACTION_CONFIGURATION_CHANGED(界面语言、设备方向等)
     插入耳机时	            Intent.ACTION_HEADSET_PLUG
     未正确移除SD卡       	Intent.ACTION_MEDIA_BAD_REMOVAL(正确移除方法:设置--SD卡和设备内存--卸载SD卡)
     插入外部储存装置	        Intent.ACTION_MEDIA_CHECKING（如SD卡）
     成功安装APK	            Intent.ACTION_PACKAGE_ADDED
     成功删除APK	            Intent.ACTION_PACKAGE_REMOVED
     重启设备	            Intent.ACTION_REBOOT
     屏幕被关闭	            Intent.ACTION_SCREEN_OFF
     屏幕被打开	            Intent.ACTION_SCREEN_ON
     关闭系统时	            Intent.ACTION_SHUTDOWN
     重启设备	            Intent.ACTION_REBOOT

     */
}
