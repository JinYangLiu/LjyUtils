package com.ljy.ljyutils.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ljy.adapter.LjyBaseAdapter;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.activity.AnimatorActivity;
import com.ljy.ljyutils.activity.AppUpdateActivity;
import com.ljy.ljyutils.activity.ArgueProgressActivity;
import com.ljy.ljyutils.activity.BallActivity;
import com.ljy.ljyutils.activity.BezierActivity;
import com.ljy.ljyutils.activity.BindDataActivity;
import com.ljy.ljyutils.activity.BroadcastActivity;
import com.ljy.ljyutils.activity.BuglyDemoActivity;
import com.ljy.ljyutils.activity.CalendarActivity;
import com.ljy.ljyutils.activity.CaptchaActivity;
import com.ljy.ljyutils.activity.ConstraintLayoutActivity;
import com.ljy.ljyutils.activity.DemoDa2Activity;
import com.ljy.ljyutils.activity.DialogDemoActivity;
import com.ljy.ljyutils.activity.DoodleActivity;
import com.ljy.ljyutils.activity.DrawerActivity;
import com.ljy.ljyutils.activity.FishActivity;
import com.ljy.ljyutils.activity.FragmentDemoActivity;
import com.ljy.ljyutils.activity.GestureLockActivity;
import com.ljy.ljyutils.activity.GifActivity;
import com.ljy.ljyutils.activity.GlideUtilActivity;
import com.ljy.ljyutils.activity.GreenDaoActivity;
import com.ljy.ljyutils.activity.JigsawActivity;
import com.ljy.ljyutils.activity.LottieActivity;
import com.ljy.ljyutils.activity.MusicActivity;
import com.ljy.ljyutils.activity.PDFUploadActivity;
import com.ljy.ljyutils.activity.PhotoActivity;
import com.ljy.ljyutils.activity.PicUploadActivity;
import com.ljy.ljyutils.activity.RadarViewActivity;
import com.ljy.ljyutils.activity.RefreshListViewActivity;
import com.ljy.ljyutils.activity.RefreshRecyclerViewActivity;
import com.ljy.ljyutils.activity.RetrofitActivity;
import com.ljy.ljyutils.activity.ServiceActivity;
import com.ljy.ljyutils.activity.SideIndexBarActivity;
import com.ljy.ljyutils.activity.SlidingMenuActivity;
import com.ljy.ljyutils.activity.UseUtilsActivity;
import com.ljy.ljyutils.activity.VideoLiveWallpaperActivity;
import com.ljy.ljyutils.activity.VideoPlayerActivity;
import com.ljy.ljyutils.activity.ViewSizeActivity;
import com.ljy.ljyutils.activity.VoteActivity;
import com.ljy.ljyutils.activity.WebViewTestActivity;
import com.ljy.ljyutils.activity.XmlParserTestActivity;
import com.ljy.ljyutils.service.TimerService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.recyclerView_main)
    RecyclerView mRecyclerView;
    private List<MainIntentBean> mList = new ArrayList<>();
    private String IS_NIGHT = "IS_NIGHT";
    private boolean isNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(mActivity);
        isNight = getSpUtilInstance().get(IS_NIGHT, false);
        setNightMode(isNight);

        //设置首页不能右滑退出
        getSwipeBackLayout().setEnableGesture(false);

        initData();
        initView();

        //启动Android定时器，并且启动服务
        TimerService.getConnet(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止由AlarmManager启动的循环
        TimerService.stop(this);
        //停止由服务启动的循环
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent);
    }

    private void initData() {
        String[] textArr = new String[]{"Utils的使用", "GlideUtil的使用", "setViewSize的使用",
                "手势锁view的使用", "雷达图view的使用", "辩论view的使用", "投票view的使用",
                "刷新和加载更多ListView的使用", "刷新和加载更多RecycleView的使用", "拍照/选取图片&&图片处理压缩等",
                "gifView的使用", "videoPlayer的使用", "dagger2的使用", "retrofit的使用",
                "fishView的使用", "broadcast的使用", "service的使用", "fragment的使用",
                "GreenDao的使用", "音乐播放demo", "Lottie的demo", "calendar的demo",
                "ballView的使用", "贝塞尔曲线的使用", "app更新", "夜间模式", "DataBinding的使用",
                "一个涂鸦画板", "上传图片", "buglyDemo", "验证码demo",
                "设置壁纸", "DrawerLayout实现侧滑菜单", "SlidingMenu实现侧滑菜单", "dialog的使用",
                "动画的使用", "ConstraintLayout的使用", "拼图view的使用","pdf文件上传",
                "分组ListView+索引条", "webView测试","xml解析"};

        Class[] classArr = new Class[]{UseUtilsActivity.class, GlideUtilActivity.class, ViewSizeActivity.class,
                GestureLockActivity.class, RadarViewActivity.class, ArgueProgressActivity.class, VoteActivity.class,
                RefreshListViewActivity.class, RefreshRecyclerViewActivity.class, PhotoActivity.class,
                GifActivity.class, VideoPlayerActivity.class, DemoDa2Activity.class, RetrofitActivity.class,
                FishActivity.class, BroadcastActivity.class, ServiceActivity.class, FragmentDemoActivity.class,
                GreenDaoActivity.class, MusicActivity.class, LottieActivity.class, CalendarActivity.class,
                BallActivity.class, BezierActivity.class, AppUpdateActivity.class, null, BindDataActivity.class,
                DoodleActivity.class, PicUploadActivity.class, BuglyDemoActivity.class, CaptchaActivity.class,
                VideoLiveWallpaperActivity.class, DrawerActivity.class, SlidingMenuActivity.class, DialogDemoActivity.class,
                AnimatorActivity.class, ConstraintLayoutActivity.class, JigsawActivity.class, PDFUploadActivity.class,
                SideIndexBarActivity.class, WebViewTestActivity.class, XmlParserTestActivity.class};

        for (int i = 0; i < textArr.length; i++) {
            MainIntentBean bean = new MainIntentBean(textArr[i], classArr[i]);
            mList.add(bean);
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(new LjyBaseAdapter<MainIntentBean>(mContext, mList, mRecyclerView, R.layout.layou_item_main) {

            @Override
            public void convert(LjyViewHolder holder, final MainIntentBean item) {
                holder.setText(R.id.textViewItem, item.getTextInfo());
                holder.setOnClickListener(R.id.itemRoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getTextInfo().equals("夜间模式")) {
                            setNight();
                        } else {
                            startActivity(new Intent(mContext, item.getIntentClass()));
                        }
                    }
                });

            }

        });
    }

    private void setNight() {
        isNight = !isNight;
        setNightMode(isNight);
        getSpUtilInstance().save(IS_NIGHT, isNight);
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    public void setNightMode(boolean isNight) {
        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private static class MainIntentBean {

        String textInfo;
        Class intentClass;

        MainIntentBean(String textInfo, Class intentClass) {
            this.textInfo = textInfo;
            this.intentClass = intentClass;
        }

        public String getTextInfo() {
            return textInfo;
        }

        Class getIntentClass() {
            return intentClass;
        }

    }

}
