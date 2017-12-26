package com.ljy.lib;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Mr.LJY on 2017/12/25.
 *
 * 用于打印log的辅助工具类, 默认定位
 */

public class LjyLogUtil {
    /**
     * 是否开启debug,true则打印log
     */
    private static boolean isDebug = true;
    /**
     *  是否显示Log定位
     */
    private static boolean isShowLineNum = true;
    /**
     * 默认的Tag
     */
    private static String finalTag="LJY_LOG";


    /**
     * 错误
     *
     * @param msg
     */
    public static void e( String msg) {
        e(null,msg);
    }
    private static void e(String tag, String msg) {
        if (isDebug()) {
            if (TextUtils.isEmpty(tag))
                tag=finalTag;
            if (isShowLineNum) {
                StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
                Log.e(tag, "(" + targetStackTraceElement.getFileName() + ":"
                        + targetStackTraceElement.getLineNumber() + ")");
            }
            Log.e(tag, "---->"+msg);
        }
    }


    /**
     * 信息
     *
     * @param msg
     */
    public static void i( String msg) {
        i(null,msg);
    }
    public static void i(String tag, String msg) {
        if (isDebug()) {
            if (TextUtils.isEmpty(tag))
                tag=finalTag;
            if (isShowLineNum) {
                StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
                Log.i(tag, "(" + targetStackTraceElement.getFileName() + ":"
                        + targetStackTraceElement.getLineNumber() + ")");
            }
            Log.i(tag,"---->"+ msg);
        }
    }

    /**
     * 警告
     *
     * @param msg
     */
    public static void w( String msg) {
        w(null,msg);
    }
    private static void w(String tag, String msg) {
        if (isDebug()) {
            if (TextUtils.isEmpty(tag))
                tag=finalTag;
            if (isShowLineNum) {
                StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
                Log.w(tag, "(" + targetStackTraceElement.getFileName() + ":"
                        + targetStackTraceElement.getLineNumber() + ")");
            }
            Log.w(tag, "---->"+msg);
        }
    }




    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(LjyLogUtil.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
    public static boolean isDebug() {
        return isDebug;
    }
    public static void setDebug(boolean isDebug) {
        LjyLogUtil.isDebug = isDebug;
    }

    public static boolean isIsShowLineNum() {
        return isShowLineNum;
    }

    public static void setIsShowLineNum(boolean isShowLineNum) {
        LjyLogUtil.isShowLineNum = isShowLineNum;
    }

    public static String getFinalTag() {
        return finalTag;
    }

    public static void setFinalTag(String finalTag) {
        LjyLogUtil.finalTag = finalTag;
    }
}
