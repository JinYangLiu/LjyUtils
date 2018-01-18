package com.ljy.ljyutils.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.service.PlayMusicService;
import com.ljy.util.LjyTimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 做个简单的音乐播放
 */
public class MusicActivity extends AppCompatActivity {
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    private int mLastProgress = 0;
    private BroadcastReceiver broadcastReceiver;
    private boolean isDraggingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        initService();
        initView();
    }

    private void initService() {
        Intent intent = new Intent(this, PlayMusicService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    public static String action_act = "com.ljy.ljyutils.activity.broadcastReceiver";
    public static String action_act_seekto = "com.ljy.ljyutils.activity.broadcastReceiver.seekTo";

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (action_act.equals(intent.getAction())) {
                    int duration = intent.getIntExtra("duration", -1);
                    if (duration >= 0) {
                        tvTotalTime.setText(LjyTimeUtil.timestampToDate(duration, "mm:ss"));
                        sbProgress.setMax(duration);
                    }
                    int currentTime = intent.getIntExtra("currentTime", -1);
                    if (currentTime > 0) {
                        if (!isDraggingProgress)
                            sbProgress.setProgress(currentTime);
                    }

                }else if (action_act_seekto.equals(intent.getAction())){
                    isDraggingProgress = false;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action_act);
        intentFilter.addAction(action_act_seekto);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initView() {
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                    tvCurrentTime.setText(LjyTimeUtil.timestampToDate(progress, "mm:ss"));
                    mLastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDraggingProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Intent intent = new Intent();
                intent.setAction(PlayMusicService.action_ser);
                intent.putExtra("type", PlayMusicService.MUSIC_SEEK);
                intent.putExtra("seekTo", seekBar.getProgress());
                sendBroadcast(intent);
            }
        });
    }

    public void onMusicBtnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                if (ivPlay.isSelected()) {
                    ivPlay.setSelected(false);
                    playMusic(PlayMusicService.MUSIC_PAUSE);
                } else {
                    ivPlay.setSelected(true);
                    playMusic(PlayMusicService.MUSIC_PLAY);
                }
                break;
            case R.id.iv_last:

                break;
            case R.id.iv_next:

                break;
        }
    }

    private int resId = R.raw.am;

    private void playMusic(int type) {

        Intent intent = new Intent();
        intent.setAction(PlayMusicService.action_ser);
        intent.putExtra("type", type);
        intent.putExtra("resId", resId);
        intent.putExtra("isLooping", false);
        sendBroadcast(intent);

    }
}
