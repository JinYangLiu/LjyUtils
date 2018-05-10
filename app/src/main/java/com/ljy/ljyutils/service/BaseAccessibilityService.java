package com.ljy.ljyutils.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import com.ljy.util.LjyLogUtil;

import java.util.List;

/**
 * Created by LJY on 2018/5/9 11:25
 * <p>
 * AccessibilityService,辅助功能,根据官方的介绍，是指开发者通过增加类似contentDescription的属性，
 * 从而在不修改代码的情况下，让残障人士能够获得使用体验的优化，大家可以打开AccessibilityService
 * 来试一下，点击区域，可以有语音或者触摸的提示，帮助残障人士使用App。
 * 当然，现在AccessibilityService已经基本偏离了它设计的初衷，至少在国内是这样，
 * 越来越多的App借用AccessibilityService来实现了一些其它功能，甚至是灰色产品。
 */
public class BaseAccessibilityService extends AccessibilityService {
    private static BaseAccessibilityService mInstance;
    private Context mContext;
    private AccessibilityManager mAccessibilityManager;
    private Handler handler = new Handler();
    public static final String COMMAND_ACTION = "COMMAND_ACTION";
    public static final int COMMAND_BACK = 1;
    public static final int COMMAND_HOME = 2;


    public static BaseAccessibilityService getInstance() {
        if (mInstance == null) {
            mInstance = new BaseAccessibilityService();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LjyLogUtil.i("event: " + event.toString());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int commandCode = intent.getIntExtra(COMMAND_ACTION, COMMAND_BACK);
            switch (commandCode) {
                case 1://COMMAND_BACK
                    performBackClick();
                    break;
                case 2://COMMAND_HOME
                    performHomeClick();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 检查当前辅助服务是否启用
     */
    public boolean isAccessibilityEnable(String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServiceInfos =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServiceInfos) {
            LjyLogUtil.i("info.getId():" + info.getId());
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前往开启辅助服务界面
     */
    public void goAccess() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 模拟点击事件
     */
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    /**
     * 模拟返回操作
     * <p>
     * 开始直接在Activity中调用,并不生效,然后各种找问题
     * 最后发现,在本service的onStartCommand中调用是可以生效的,然后外部通过intent给本service发送命令
     */
    private void performBackClick() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }, 800);
    }

    /**
     * 模拟Home操作
     */
    private void performHomeClick() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performGlobalAction(GLOBAL_ACTION_HOME);
            }
        }, 800);
    }

    /**
     * 模拟下滑操作
     */
    private void performScrollBackward() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }
        }, 800);
    }

    /**
     * 模拟上滑操作
     */
    private void performScrollForward() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        }, 800);

    }

    /**
     * 查找对应文本的View
     */
    public AccessibilityNodeInfo findViewByText(String text) {
        return findViewByText(text, false);
    }

    /**
     * 查找对应文本的View
     */
    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return null;
        List<AccessibilityNodeInfo> nodeInfoList =
                accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public AccessibilityNodeInfo findViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    public void clickTextViewByText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void clickTextViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    public void inputText(AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }


}
