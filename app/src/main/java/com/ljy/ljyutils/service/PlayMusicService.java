package com.ljy.ljyutils.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.activity.MusicActivity;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

/**
 * Created by Mr.LJY on 2018/1/18.
 */

public class PlayMusicService extends Service {

    private MediaPlayer mediaPlayer;
    public static int MUSIC_PLAY = 1;//开始
    public static int MUSIC_PAUSE = 2;//暂停
    public static int MUSIC_STOP = 3;//结束
    public static int MUSIC_SEEK = 4;//拖动
    private boolean isStop = true;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                //发送广播设置播放进度
                Intent intent = new Intent();
                intent.putExtra("currentTime", mediaPlayer.getCurrentPosition());
                intent.setAction(MusicActivity.action_act);
                sendBroadcast(intent);
            }
            mHandler.postDelayed(this, 300);//每0.3秒更新一次
        }
    };
    private BroadcastReceiver broadcastReceiver;
    public static String action_ser = "com.ljy.ljyutils.service.broadcastReceiver";

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //添加播放完成的监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LjyToastUtil.toast(getApplicationContext(), "播放结束咯~");
                }
            });
        }
        initBroadcastReceiver();

    }

    private void showNotification(boolean isPlaying) {
        //添加下列代码将后台Service变成前台Service
        //构建"点击通知后打开MainActivity"的Intent对象
        Intent notificationIntent = new Intent(this, MusicActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //新建Builer对象
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setCustomContentView(getRemoteViews(isPlaying))
                .build();
        startForeground(1, notification);//让Service变成前台Service,并在系统的状态栏显示出来
    }

    private RemoteViews getRemoteViews( boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.tv_title, "歌曲名：暧昧");
        remoteViews.setTextViewText(R.id.tv_subtitle, "演唱者：薛之谦");
        remoteViews.setImageViewResource(R.id.iv_play_pause, isPlaying?R.drawable.ic_status_bar_pause_dark:R.drawable.ic_status_bar_play_dark);
        Intent intent = new Intent();
        intent.setAction(PlayMusicService.action_ser);
        intent.putExtra("type", isPlaying?MUSIC_PAUSE:MUSIC_PLAY);
        intent.putExtra("isChangeUI", true);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, playPendingIntent);
        Intent intent2 = new Intent();
        intent2.setAction(PlayMusicService.action_ser);
        intent2.putExtra("type", MUSIC_STOP);
        PendingIntent playPendingIntent2 = PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_finish, playPendingIntent2);
        return remoteViews;
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (action_ser.equals(intent.getAction())) {
                    playMusic(intent);
                    boolean isChangeUI=intent.getBooleanExtra("isChangeUI",false);
                    LjyLogUtil.i("isChangeUI:"+isChangeUI);
                    if (isChangeUI){
                        //发送广播设置UI更新
                        Intent intent2 = new Intent();
                        intent2.putExtra("isToPlay", intent.getIntExtra("type",-1)==1);
                        intent2.setAction(MusicActivity.action_act_changeUI);
                        sendBroadcast(intent2);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action_ser);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
//        返回值规定此startservice是哪种类型，粘性的还是非粘性的
//        START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
//        START_NOT_STICKY:非粘性，遇到异常停止不会重启
//        START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
    }

    private void playMusic(Intent intent) {
        switch (intent.getIntExtra("type", -1)) {
            case 1:
                if (isStop) {//重新播放
                    //重置mediaPlayer
                    mediaPlayer.reset();
                    //绑定需要播放的资源
                    String musicUri = intent.getStringExtra("uri");
                    if (TextUtils.isEmpty(musicUri)) {
                        int musicResId = intent.getIntExtra("resId", -1);
                        if (musicResId != -1)
                            mediaPlayer = MediaPlayer.create(this, musicResId);
                    } else {
                        mediaPlayer = MediaPlayer.create(this, Uri.parse(musicUri));
                    }

                    //开始播放
                    mediaPlayer.start();
                    //是否循环播放
                    boolean isLooping = intent.getBooleanExtra("isLooping", false);
                    mediaPlayer.setLooping(isLooping);
                    //发送广播设置总长
                    Intent intent2 = new Intent();
                    intent2.putExtra("duration", mediaPlayer.getDuration());
                    intent2.setAction(MusicActivity.action_act);
                    sendBroadcast(intent2);
                    //更新进度
                    mHandler.post(mRunnable);
                    isStop = false;
                    //显示通知，将服务设为前台
                    showNotification(true);
                } else if (!isStop && mediaPlayer != null && !mediaPlayer.isPlaying()) {//继续播放
                    mediaPlayer.start();
                    //更新进度
                    mHandler.post(mRunnable);
                    showNotification(true);
                }
                break;
            case 2:
                //暂停
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mHandler.removeCallbacks(mRunnable);
                }
                showNotification(false);
                break;
            case 3:
                //停止
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    isStop = true;
                    mHandler.removeCallbacks(mRunnable);
                }
                stopSelf();
                break;
            case 4://拖动后设置播放进度
                if (mediaPlayer != null && intent != null) {
                    int seekTo = intent.getIntExtra("seekTo", -1);
                    if (seekTo >= 0)
                        mediaPlayer.seekTo(seekTo);
                }
                //发送广播设置拖拽后可以更新进度
                Intent intent2 = new Intent();
                intent2.setAction(MusicActivity.action_act_seekto);
                sendBroadcast(intent2);
                break;

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
