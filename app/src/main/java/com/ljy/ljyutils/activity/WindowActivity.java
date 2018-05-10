package com.ljy.ljyutils.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.service.BaseAccessibilityService;
import com.ljy.ljyutils.service.FloatingWindowService;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
import com.ljy.util.LjyScreenUtils;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyMDDialogManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * window:
 * 1.具体实现类PhoneWindow
 * 2.通过WindowManager创建,window具体实现在WindowManagerService中
 * 3.Android中所有识图都是通过window来呈现的(包括Activity,Dialog,Toast),window实际是View的直接管理者,
 * 包括事件分发也是由window-->DecorView-->View
 */
public class WindowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addBtn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        new LjyMDDialogManager(mActivity).alertSingleButton("提示", "请先开启悬浮窗权限", "去开启",
                                new LjyMDDialogManager.OnPositiveListener() {
                                    @Override
                                    public void positive() {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 10);
                                    }
                                }, false);
                    } else {
                        addBtn();
                    }
                } else {
                    addBtn();
                }
                break;
            case R.id.btn_delBtn:
                Intent hide = new Intent(this, FloatingWindowService.class);
                hide.putExtra(FloatingWindowService.OPERATION, FloatingWindowService.OPERATION_HIDE);
                startService(hide);
                break;
        }
    }

    private void addBtn() {
        Intent show = new Intent(this, FloatingWindowService.class);
        show.putExtra(FloatingWindowService.OPERATION, FloatingWindowService.OPERATION_SHOW);
        startService(show);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    LjyToastUtil.toast(mContext, "not granted");
                } else {
                    addBtn();
                }
            }
        }
    }

}
