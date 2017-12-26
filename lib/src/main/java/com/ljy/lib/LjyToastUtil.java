package com.ljy.lib;

import android.content.Context;
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
}