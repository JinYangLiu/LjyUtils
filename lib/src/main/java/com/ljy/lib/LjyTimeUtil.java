package com.ljy.lib;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjyTimeUtil {
    /**
     * 将时间戳转换为指定格式字符串,
     *
     * @param time
     * @param pattern 如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String timestampToDate(long time, String pattern) {
        if (TextUtils.isEmpty(pattern))
            pattern="yyyy-MM-dd HH:mm:ss";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 判断存储的时间戳与当前时间戳是否同一天
     *
     */
    public static boolean isSameDay( long lastTimeMill, long currentTimeMill) {
        String lastTime = timestampToDate(lastTimeMill, "yyyyMMdd");
        String currentTime = timestampToDate(currentTimeMill, "yyyyMMdd");
        return currentTime != null && currentTime.equals(lastTime);
    }
}
