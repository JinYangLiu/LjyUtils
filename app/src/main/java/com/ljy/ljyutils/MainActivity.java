package com.ljy.ljyutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljy.ljyutils.activity.ArgueProgressActivity;
import com.ljy.ljyutils.activity.BroadcastActivity;
import com.ljy.ljyutils.activity.CalendarActivity;
import com.ljy.ljyutils.activity.DemoDa2Activity;
import com.ljy.ljyutils.activity.FishActivity;
import com.ljy.ljyutils.activity.FragmentDemoActivity;
import com.ljy.ljyutils.activity.GestureLockActivity;
import com.ljy.ljyutils.activity.GifActivity;
import com.ljy.ljyutils.activity.GlideUtilActivity;
import com.ljy.ljyutils.activity.GreenDaoActivity;
import com.ljy.ljyutils.activity.LottieActivity;
import com.ljy.ljyutils.activity.MusicActivity;
import com.ljy.ljyutils.activity.PhotoActivity;
import com.ljy.ljyutils.activity.RadarViewActivity;
import com.ljy.ljyutils.activity.RefreshListViewActivity;
import com.ljy.ljyutils.activity.RefreshRecyclerViewActivity;
import com.ljy.ljyutils.activity.RetrofitActivity;
import com.ljy.ljyutils.activity.ServiceActivity;
import com.ljy.ljyutils.activity.UseUtilsActivity;
import com.ljy.ljyutils.activity.VideoPlayerActivity;
import com.ljy.ljyutils.activity.ViewSizeActivity;
import com.ljy.ljyutils.activity.VoteActivity;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMainBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_app_util:
                startActivity(new Intent(mContext, UseUtilsActivity.class));
                break;
            case R.id.btn_glide_util:
                startActivity(new Intent(mContext, GlideUtilActivity.class));
                break;
            case R.id.btn_setViewSize:
                startActivity(new Intent(mContext, ViewSizeActivity.class));
                break;
            case R.id.btn_gestureLockView:
                startActivity(new Intent(mContext, GestureLockActivity.class));
                break;
            case R.id.btn_radarView:
                startActivity(new Intent(mContext, RadarViewActivity.class));
                break;
            case R.id.btn_argueView:
                startActivity(new Intent(mContext, ArgueProgressActivity.class));
                break;
            case R.id.btn_vote:
                startActivity(new Intent(mContext, VoteActivity.class));
                break;
            case R.id.btn_refreshListView:
                startActivity(new Intent(mContext, RefreshListViewActivity.class));
                break;
            case R.id.btn_refreshRecycleView:
                startActivity(new Intent(mContext, RefreshRecyclerViewActivity.class));
                break;
            case R.id.btn_picture:
                startActivity(new Intent(mContext, PhotoActivity.class));
                break;
            case R.id.btn_gif:
                startActivity(new Intent(mContext, GifActivity.class));
                break;
            case R.id.btn_video:
                startActivity(new Intent(mContext, VideoPlayerActivity.class));
                break;
            case R.id.btn_dagger2:
                startActivity(new Intent(mContext, DemoDa2Activity.class));
                break;
            case R.id.btn_retrofit:
                startActivity(new Intent(mContext, RetrofitActivity.class));
                break;
            case R.id.btn_fish:
                startActivity(new Intent(mContext, FishActivity.class));
                break;
            case R.id.btn_broadcast:
                startActivity(new Intent(mContext, BroadcastActivity.class));
                break;
            case R.id.btn_service:
                startActivity(new Intent(mContext, ServiceActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(mContext, FragmentDemoActivity.class));
                break;
            case R.id.btn_greenDao:
                startActivity(new Intent(mContext, GreenDaoActivity.class));
                break;
            case R.id.btn_music:
                startActivity(new Intent(mContext, MusicActivity.class));
                break;
            case R.id.btn_lottie:
                startActivity(new Intent(mContext, LottieActivity.class));
                break;
            case R.id.btn_calendar:
                startActivity(new Intent(mContext, CalendarActivity.class));
                break;
            default:
                break;
        }
    }
}
