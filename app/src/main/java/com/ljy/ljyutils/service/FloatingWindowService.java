package com.ljy.ljyutils.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyScreenUtils;
import com.ljy.util.LjyToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJY on 2018/5/10 10:19
 */
public class FloatingWindowService extends Service {
    public static String OPERATION="OPERATION";
    public static int OPERATION_HIDE=0;
    public static int OPERATION_SHOW=1;
    private WindowManager windowManager;
    private int btnSize;
    private boolean isAddBtn = false;
    private Button button;

    @Override
    public void onCreate() {
        super.onCreate();
        btnSize = LjyDensityUtil.dp2px(getApplicationContext(), 50);
        BaseAccessibilityService.getInstance().init(this);
        initBtn();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null) {
            int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
            switch (operation) {
                case 1:
                    addBtn();
                    break;
                case 0:
                    removeBtn();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void removeBtn() {
        windowManager.removeView(button);
        isAddBtn=false;
    }

    /**
     * AndroidManifest.xml中需要添加权限SYSTEM_ALERT_WINDOW
     * //flag:
     * //FLAG_NOT_FOCUSABLE:表示window不需要获取焦点,页不需要接收各种输入事件,此标记会同时启用 FLAG_NOT_TOUCH_MODAL,
     * //最终事件会直接传递给下层的具有焦点的Window
     * //FLAG_NOT_TOUCH_MODAL: 此模式下,系统会将当前window区域以外的单击事件传递给底层的window,当前window区域
     * //以内的单击事件自己处理,要谨慎使用,可能导致其他window无法收到单击事件
     * //FLAG_SHOW_WHEN_LOCKED: 让window显示在锁屏界面
     * //type:
     * //
     */
    @SuppressLint("ClickableViewAccessibility")
    private void addBtn() {
        if (windowManager == null) {
            windowManager = (WindowManager) getApplicationContext()
                    .getSystemService(getApplication().WINDOW_SERVICE);
            //这里如果用getWindowManager, 退出当前应用后, 悬浮窗就会消失
        }
        final WindowManager.LayoutParams layoutParams = getLayoutParams();
        if (isAddBtn) {
            windowManager.updateViewLayout(button, layoutParams);
        } else {
            isAddBtn = true;
            windowManager.addView(button, layoutParams);
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            LjyLogUtil.i("x:" + x + ", y:" + y);
                            layoutParams.x = x - v.getWidth() / 2;
                            layoutParams.y = y - v.getHeight();
                            windowManager.updateViewLayout(button, layoutParams);
                            break;
                    }
                    return false;
                }
            });

        }
    }

    private void initBtn() {
        final String serviceName = "com.ljy.ljyutils/.service.BaseAccessibilityService";
        button = new Button(this);
        button.setBackground(getResources().getDrawable(R.drawable.ic_music));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //方式1:
                //需要INJECT_EVENTS权限,而这个权限又需要系统签名,弃之
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Instrumentation inst = new Instrumentation();
//                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                        }
//                    }).start();
                //方式2:只对本app生效,无法满足我的需求呀
//                LjyToastUtil.toast(getApplicationContext(), "点击了btn,执行back操作");
//                try {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //方式3:AccessibilityService
                boolean isAccessibilityEnable = BaseAccessibilityService.getInstance().isAccessibilityEnable(serviceName);
                LjyToastUtil.toast(getApplicationContext(), "点击了Btn," + (isAccessibilityEnable ? "执行back操作" : "请求权限"));
                if (isAccessibilityEnable) {
                    Intent intentBack = new Intent(getApplicationContext(), BaseAccessibilityService.class);
                    intentBack.putExtra(BaseAccessibilityService.COMMAND_ACTION,BaseAccessibilityService.COMMAND_BACK);
                    startService(intentBack);
                } else {
                    BaseAccessibilityService.getInstance().goAccess();
                }


            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //方式1:
//                LjyToastUtil.toast(getApplicationContext(), "长按了btn,执行home操作");
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
                //方式2:
                boolean isAccessibilityEnable = BaseAccessibilityService.getInstance().isAccessibilityEnable(serviceName);
                LjyToastUtil.toast(getApplicationContext(), "长按了Btn," + (isAccessibilityEnable ? "执行home操作" : "请求权限"));
                if (isAccessibilityEnable) {
                    Intent intentBack = new Intent(getApplicationContext(), BaseAccessibilityService.class);
                    intentBack.putExtra(BaseAccessibilityService.COMMAND_ACTION,BaseAccessibilityService.COMMAND_HOME);
                    startService(intentBack);
                } else {
                    BaseAccessibilityService.getInstance().goAccess();
                }
                return true;
            }
        });
        button.setAllCaps(false);
        button.setText("Mr.L");
    }

    @NonNull
    private WindowManager.LayoutParams getLayoutParams() {
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                btnSize, btnSize, 0, 0, PixelFormat.TRANSPARENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        layoutParams.gravity = Gravity.CENTER;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = LjyScreenUtils.getScreenWidth(getApplicationContext());
        layoutParams.y = LjyScreenUtils.getScreenHeight(getApplicationContext()) / 2;
        // type 设置 Window 类别（层级）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return layoutParams;
    }



    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


}
