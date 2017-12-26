package com.ljy.lib;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjyScreenUtil {
    /**
     * 获得屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        context=context.getApplicationContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    /**
     * 获得屏幕的高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        context=context.getApplicationContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_height = dm.heightPixels;
        return w_height;
    }
}
