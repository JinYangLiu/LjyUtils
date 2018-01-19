package com.ljy.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.ljy.bean.CalendarBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/25.
 *
 * 时间&日期相关工具类
 */

public class LjyTimeUtil {
    /**
     * 将时间戳转换为指定格式字符串
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
     * 判断两个时间戳是否同一天
     *
     */
    public static boolean isSameDay( long lastTimeMill, long currentTimeMill) {
        String lastTime = timestampToDate(lastTimeMill, "yyyyMMdd");
        String currentTime = timestampToDate(currentTimeMill, "yyyyMMdd");
        return currentTime != null && currentTime.equals(lastTime);
    }


    @SuppressLint("WrongConstant")
    public static List<CalendarBean> getDaysListOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return getDaysListOfMonth(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取当前月份的日期列表
     * @param year
     * @param month
     * @return
     */
    public static List<CalendarBean> getDaysListOfMonth(int year, int month) {

        List<CalendarBean> list = new ArrayList<>();

        int daysOfMonth = getDaysOfCertainMonth(year, month);

        for (int i = 0; i < daysOfMonth; i++) {
            CalendarBean bean = generateCalendarBean(year, month, i + 1);
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取具体月份的最大天数
     *
     * @param year
     * @param month
     * @return
     */
    @SuppressLint("WrongConstant")
    public static int getDaysOfCertainMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 构建具体一天的对象
     * @param year
     * @param month
     * @param day
     * @return
     */
    @SuppressLint("WrongConstant")
    public static CalendarBean generateCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        return new CalendarBean(year, month, day);
    }

    /**
     * 获取具体一天对应的星期
     *
     * @param year
     * @param month
     * @param day
     * @return 1-7(周日-周六)
     */
    @SuppressLint("WrongConstant")
    public static int getWeekDayOnCertainDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
