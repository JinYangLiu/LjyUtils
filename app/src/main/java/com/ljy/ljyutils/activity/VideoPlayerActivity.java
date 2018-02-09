package com.ljy.ljyutils.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyGlideUtil;
import com.ljy.view.LjyVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends BaseActivity {
    @BindView(R.id.video_player)
    LjyVideoPlayer videoPlayer;
    private String imgUrl = "https://avatars1.githubusercontent.com/u/19702574?s=460&v=4";
    private String playUrl = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=70096&editionType=default&source=aliyun";
    private boolean isFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(mActivity);
        initVideo();
    }

    private void initVideo() {

        //设置加载时封面
        ImageView ivCoverVideo = new ImageView(mContext);
        ivCoverVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LjyGlideUtil.loadImg(mContext, imgUrl, ivCoverVideo);
        videoPlayer.setThumbImageView(ivCoverVideo);
        //一全屏就锁屏横屏
        videoPlayer.setLockLand(true);
        videoPlayer.setNeedLockFull(true);
        //设置url并开始
        videoPlayer.setUp(playUrl, false, "测试视频");
        videoPlayer.startPlayLogic();
        //全屏和返回的监听
        playerButtonListener();

    }

    private void playerButtonListener() {
        //设置旋转
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                isFullScreen = true;
                videoPlayer.startWindowFullscreen(mContext, false, true);
            }
        });
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        videoPlayer.setBackFromFullScreenListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFullScreen = false;
                videoPlayer.quitFullScreen();
            }
        });
        videoPlayer.getBackButton().setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoView.releaseAllVideos();
    }

    public void onVideoClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                videoPlayer.onVideoResume();
                break;
            case R.id.pause:
                videoPlayer.onVideoPause();
                break;
            case R.id.resume:
                videoPlayer.startPlayLogic();
                break;
            case R.id.small:
                //小窗口播放
                int size = CommonUtil.dip2px(mContext, 150);
                videoPlayer.showSmallVideo(new Point(size*2, size), false, true);
                break;
            case R.id.hidesmall:
                videoPlayer.hideSmallVideo();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下了back键
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isFullScreen) {
//                isFullScreen = false;
//                videoPlayer.quitFullScreen();
            } else {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
