package com.ljy.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Mr.LJY on 2017/12/25.
 * <p>
 * 用于打印log的辅助工具类, 默认定位
 */

public class LjyLogUtil {
    /**
     * 是否开启debug,true则打印log
     */
    private static boolean isLog = true;
    /**
     * 是否显示Log定位
     */
    private static boolean showLineNum = true;
    /**
     * 默认的Tag
     */
    private static String logTag = "LJY_LOG";

    /**
     * 是否叠加log信息
     */
    private static boolean isAppendLogMsg = false;

    /**
     * log类型的枚举
     */
    enum LogType {
        LOG_I, LOG_W, LOG_E
    }


    /**
     * 信息
     *
     * @param msg
     */
    public static void i(CharSequence msg) {
        log(null, msg,LogType.LOG_I);
    }

    /**
     * 警告
     *
     * @param msg
     */
    public static void w(CharSequence msg) {
        log(null, msg,LogType.LOG_W);
    }

    /**
     * 错误
     *
     * @param msg
     */
    public static void e(CharSequence msg) {
        log(null, msg,LogType.LOG_E);
    }

    /**
     * 打印log的基础方法
     * @param tag
     * @param msg
     * @param logType
     */
    public static void log(String tag, CharSequence msg,LogType logType) {
        //判断是否打印log
        if (isLog()) {
            //判断是否有tag，没有则用默认的 LJY_LOG
            if (TextUtils.isEmpty(tag))
                tag = logTag;
            //调用打印定位的方法
            logLineNum(tag,logType);
            //打印log
            String logMsg="---->" + msg;
            switch (logType) {
                case LOG_I:
                    Log.i(tag,logMsg );
                    break;
                case LOG_W:
                    Log.w(tag, logMsg);
                    break;
                case LOG_E:
                    Log.e(tag, logMsg);
                    break;
            }
            //判断是否叠加字符串
            if (isAppendLogMsg){
                appendStr(msg);
            }
        }
    }

    private static void logLineNum(String tag, LogType logType) {
        if (showLineNum) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            String lineInfo = "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")";
            switch (logType) {
                case LOG_I:
                    Log.i(tag, lineInfo);
                    break;
                case LOG_W:
                    Log.w(tag, lineInfo);
                    break;
                case LOG_E:
                    Log.e(tag, lineInfo);
                    break;
            }
        }
    }

    /**
     * 获取log定位
     * @return
     */
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

    /**
     * 是否打印log
     *
     * @return
     */
    public static boolean isLog() {
        return isLog;
    }

    public static void setIsLog(boolean isDebug) {
        LjyLogUtil.isLog = isDebug;
    }

    /**
     * 是否显示定位
     *
     * @return
     */
    public static boolean isShowLineNum() {
        return showLineNum;
    }

    public static void setShowLineNum(boolean showLineNum) {
        LjyLogUtil.showLineNum = showLineNum;
    }

    /**
     * 设置tag
     *
     * @return
     */
    public static String getLogTag() {
        return logTag;
    }

    public static void setLogTag(String logTag) {
        LjyLogUtil.logTag = logTag;
    }

    /**
     * 用于叠加需要log的信息
     */
    private static StringBuffer logMsg = new StringBuffer();

    /**
     * 叠加
     *
     * @param info
     */
    public static void appendStr(CharSequence info) {
        logMsg.append(info);
        logMsg.append("\n");
    }

    /**
     * 获取
     *
     * @return
     */
    public static String getAllLogMsg() {
        return logMsg.toString();
    }

    /**
     * 清空
     */
    public static void clearAllLogMsg() {
        logMsg.setLength(0);
    }

    public static boolean isIsAppendLogMsg() {
        return isAppendLogMsg;
    }

    public static void setAppendLogMsg(boolean isAppendLogMsg) {
        LjyLogUtil.isAppendLogMsg = isAppendLogMsg;
    }
}
