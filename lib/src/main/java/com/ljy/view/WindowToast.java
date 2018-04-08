package com.ljy.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljy.lib.R;

/**
 * Created by Mr.LJY on 2018/4/4.
 */

public class WindowToast {
    //定义变量
    private static final int MESSAGE_WHAT = 0;
    public static final double LENGTH_SHORT = 2000.0;//2s
    public static final double LENGTH_LONG = 3500.0;//3,5s
    private static WindowToast toastCustom;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams params;
    private double time;
    private static View mView;
    private static TextView textView;
    private MyHandler mHandler;

    /**
     * 指定显示内容和时间
     *
     * @param context 可以是Activity或getApplicationContext()
     * @param text    需要提示的信息文字
     * @param time    单位:毫秒
     */
    private WindowToast(Context context, CharSequence text, long time) {
        //初始化
        this.time = time;
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        //将自定义View添加到自定义布局
        mView = customView(context, text);

        //设置布局参数
        setLayoutParams(-1);
    }

    /**
     * 指定显示内容，时间和动画ID
     *
     * @param context
     * @param text
     * @param time
     * @param resAnimId
     */
    private WindowToast(Context context, String text, double time, int resAnimId) {
        //初始化
        this.time = time;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //将自定义View添加到自定义布局
        mView = customView(context, text);

        //设置布局参数
        setLayoutParams(resAnimId);
    }

    //设置布局参数
    private void setLayoutParams(int resAnimId) {
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = resAnimId;//Animation.INFINITE
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        params.type = WindowManager.LayoutParams.TYPE_PHONE;
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        params.y = 100;//正数，数值越大，位置向上移动
        params.x = 0;
    }

    //自定义View
    private View customView(Context context, CharSequence text) {
        LinearLayout toastView = new LinearLayout(context);
        toastView.setOrientation(LinearLayout.VERTICAL);
        toastView.setBackgroundResource(R.drawable.text_view_border);
        textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(14);
        textView.setPadding(20, 5, 20, 5);
        toastView.addView(textView, 0);
        return toastView;
    }

    /**
     * 传递需要显示的参数
     *
     * @param context
     * @param text
     * @param time
     * @return
     */
    public static WindowToast makeText(Context context, CharSequence text, long time) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 需要显示文字和图片
     *
     * @param context
     * @param text
     * @param time
     * @return
     */
    public static WindowToast makeTextAndIcon(Context context, CharSequence text, long time, int resIconId) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
            LinearLayout toastView = (LinearLayout) toastCustom.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(resIconId);
            toastView.addView(imageView, 0);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 控制显示文字和动画
     *
     * @param context
     * @param text
     * @param time
     * @param resAnimId :使用方法:包名.R.style.自定义动画的样式名称
     * @return
     */
    public static WindowToast makeTextAndAnim(Context context, CharSequence text, long time, int resAnimId) {
        if (toastCustom == null) {
            toastCustom = new WindowToast(context, text, time);
        } else {
            setText(text);
        }
        return toastCustom;
    }

    /**
     * 自定义View
     *
     * @param view
     * @return
     */
    public static WindowToast setView(View view) {
        mView = view;
        return toastCustom;
    }

    /**
     * 获取默认的显示的view
     *
     * @return
     */
    public static View getView() {
        return mView;
    }

    /**
     * 修改显示的文本
     *
     * @param message
     */
    private static void setText(CharSequence message) {
        textView.setText(message);
    }

    /**
     * 自定义显示位置
     *
     * @param gravity:传递参数，如: Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM
     * @param xOffset
     * @return yOffset
     */
    public static WindowToast setGravity(int gravity, int xOffset, int yOffset) {
        params.gravity = gravity;
        params.x = xOffset;
        params.y = yOffset;
        return toastCustom;
    }


    /**
     * 调用makeText之后再调用
     */
    public void show() {
        //防止多次点击，重复添加
        if (mHandler == null) {
            mHandler = new MyHandler();
            mWindowManager.addView(mView, params);
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, (long) (time));
            /*
            在Android 7.1.1、7.1.2和去年8月发布的Android 8.0系统中，我们的方案出现了另一个异常token null is not valid，这个异常堆栈如下：
               android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
               at android.view.ViewRootImpl.setView(ViewRootImpl.java:683)
               at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:342)
               at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:94)
            token null is not valid原因分析
            这个异常其实并非是Toast的异常，而是Google对WindowManage的一些限制导致的。Android从7.1.1版本开始，
            对WindowManager做了一些限制和修改，特别是TYPE_TOAST类型的窗口，必须要传递一个token用于权限校验才允许添加.
            Toast源码在7.1.1及以上也有了变化，Toast的WindowManager.LayoutParams参数额外添加了一个token属性，
            这个属性的来源就已经在上文分析过了，它是在NMS中被初始化的，用于对添加的窗口类型进行校验。当用户禁掉通知权限时，
            由于AspectJ的存在，最终会调用我们封装的MToast，但是MToast没有经过NMS，因此无法获取到这个属性，
            另外就算我们按照NMS的方法自己生成一个token，这个token也是没有添加TYPE_TOAST权限的，最终还是无法避免这个异常的发生。
             */
        }
    }

    /**
     * 取消View的显示
     */
    private void cancel() {
        mWindowManager.removeView(mView);
        mView = null;
        toastCustom = null;
        mHandler = null;
    }

    //自定义Handler
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT:
                    cancel();
                    break;
            }
        }
    }
}
