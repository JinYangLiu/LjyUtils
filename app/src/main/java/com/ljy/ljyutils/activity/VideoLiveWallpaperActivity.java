package com.ljy.ljyutils.activity;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.service.VideoLiveWallpaper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoLiveWallpaperActivity extends BaseActivity {

    @BindView(R.id.id_cb_voice)
    CheckBox mCbVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_live_wallpaper);

        ButterKnife.bind(mActivity);

        initView();
    }

    private void initView() {
        if (mCbVoice != null) {
            mCbVoice.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                // 静音
                                VideoLiveWallpaper.voiceSilence(getApplicationContext());
                            } else {
                                VideoLiveWallpaper.voiceNormal(getApplicationContext());
                            }
                        }
                    });
        }
    }

    //btn click事件
    @SuppressLint("ResourceType")
    public void onWallPaperClick(View view) {
        switch (view.getId()) {
            case R.id.btn_videoWallPaper:
                VideoLiveWallpaper.setToWallPaper(this);
                break;
            case R.id.btn_systemWallPaper:
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "选择壁纸~"));
                break;
            case R.id.btn_wallpaperManager:
                //利用WallpaparManager,添加权限set_wallpaper
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                try {
                    wallpaperManager.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.music_bg));
//                    wallpaperManager.setResource(R.drawable.music_bg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //getSystemService(WALLPAPER_SERVICE)获取wallpaperManager会出问题
                /*
                @SuppressLint("ServiceCast")
                WallpaperManager manager =(WallpaperManager)getSystemService(WALLPAPER_SERVICE);
                try {
                    manager.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pic1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                break;
        }
    }
}
