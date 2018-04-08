package com.ljy.util;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjyToastUtil {
    private static boolean isDebug = true;
    private static Toast toast;

    /**
     * 唯一的toast
     *
     * @param context
     * @param text
     */
    public static void toast(Context context, CharSequence text) {
        toast(context, text, false);
    }

    public static void toast(Context context, CharSequence text, boolean isLong) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    text,
                    isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 不唯一的toast
     *
     * @param context
     * @param text
     */
    public static void toastEve(Context context, CharSequence text) {
        toastEve(context, text, false);
    }

    public static void toastEve(Context context, CharSequence text, boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void toastDebug(Context context, CharSequence text) {
        if (isDebug)
            toastEve(context, text);
    }

    public static void showSnackBar(View v, String info) {
        Snackbar.make(v, info, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View v, String info, View.OnClickListener listener, Snackbar.Callback callback) {
        Snackbar snackBar = Snackbar.make(v, info, Snackbar.LENGTH_SHORT);
        //设置SnackBar背景颜色
        snackBar.getView().setBackgroundColor(Color.BLACK);
        //设置按钮文字颜色
        snackBar.setActionTextColor(Color.WHITE);
        //设置点击事件
        if (listener != null)
            snackBar.setAction("点击", listener);
        //设置回调
        if (callback != null)
            snackBar.setCallback(callback).show();
    }

    public static void windowToast(Context context, CharSequence text, long time){
        WindowToast.makeText(context,text,time).show();
    }



}