package com.ljy.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.ljy.bean.CalendarBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/25.
 * <p>
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
            pattern = "yyyy-MM-dd HH:mm:ss";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 判断两个时间戳是否同一天
     */
    public static boolean isSameDay(long lastTimeMill, long currentTimeMill) {
        String lastTime = timestampToDate(lastTimeMill, "yyyyMMdd");
        String currentTime = timestampToDate(currentTimeMill, "yyyyMMdd");
        return currentTime != null && currentTime.equals(lastTime);
    }


    @SuppressLint("WrongConstant")
    public static List<CalendarBean> getDaysOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return getDaysOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取当前月份的日期列表
     *
     * @param year
     * @param month
     * @return
     */
    @SuppressLint("WrongConstant")
    public static List<CalendarBean> getDaysOfMonth(int year, int month) {

        List<CalendarBean> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DATE);

        for (int i = 1; i <= daysOfMonth; i++) {
            calendar.set(year, month - 1, i);
            CalendarBean bean = new CalendarBean(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            list.add(bean);
        }
        return list;
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
    public static int getWeekForDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 农历年的生肖
     */
    public static String getAnimalsYear(int chinaYear) {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇",
                "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(chinaYear - 4) % 12];
    }

    /**
     * 根据日期获取星座
     */
    public static String getConstellation(CalendarBean bean) {
        String[] constellationArray = {"水瓶座", "双鱼座", "白羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22,
                23, 23, 23, 23, 22, 22};
        int month = bean.getMonth() - 1;
        int day = bean.getDay();
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

    /**
     * 获取节气
     *
     * @return
     */
    public static String getSolarTerm(CalendarBean bean) {
        String[] SolarTerm = new String[]{"小寒", "大寒", "立春", "雨水",
                "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋",
                "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
        int y = bean.getYear();
        int m = bean.getMonth();
        int d = bean.getDay();
        String solarTerms;
        if (d == sTerm(y, (m - 1) * 2))
            solarTerms = SolarTerm[(m - 1) * 2];
        else if (d == sTerm(y, (m - 1) * 2 + 1))
            solarTerms = SolarTerm[(m - 1) * 2 + 1];
        else
            solarTerms = "";
        return solarTerms;
    }

    //  ===== 某年的第n个节气为几日(从0小寒起算)
    @SuppressLint("WrongConstant")
    private static int sTerm(int y, int n) {
        long[] STermInfo = new long[]{0, 21208, 42467, 63836, 85337,
                107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343,
                285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795,
                462224, 483532, 504758};
        Calendar offDate = Calendar.getInstance();
        offDate.set(1900, 0, 6, 2, 5, 0);
        long temp = offDate.getTime().getTime();
        offDate
                .setTime(new Date(
                        (long) ((31556925974.7 * (y - 1900) + STermInfo[n] * 60000L) + temp)));

        return offDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 农历年的天干地支
     */
    public static String getChineseEra(CalendarBean bean) {
//        int year = bean.getYear();
//        int month = bean.getMonth();
//        int day = bean.getDay();
        final String[] gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚",
                "辛", "壬", "癸"};
        final String[] zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午",
                "未", "申", "酉", "戌", "亥"};
//        String gz = "甲子 乙丑 丙寅 丁卯 戊辰 己巳 庚午 辛未 壬申 癸酉 " +
//                "甲戌 乙亥 丙子 丁丑 戊寅 己卯 庚辰 辛巳 壬午 癸未 " +
//                "甲申 乙酉 丙戌 丁亥 戊子 己丑 庚寅 辛卯 壬辰 癸巳 " +
//                "甲午 乙未 丙申 丁酉 戊戌 己亥 庚子 辛丑 壬寅 癸卯 " +
//                "甲辰 乙巳 丙午 丁未 戊申 己酉 庚戌 辛亥 壬子 癸丑 " +
//                "甲寅 乙卯 丙辰 丁巳 戊午 己未 庚申 辛酉 壬戌 癸亥";
//        String[] gz60 = gz.split(" ");
//        int first_year = year / 100;
//        int last_year = year % 100;
//        //从已知日期计算干支纪日的公式为：
//        //　　G = 4C + [C / 4] + 5y + [y / 4] + [3 * (M + 1) / 5] + d - 3
//        //　　Z = 8C + [C / 4] + 5y + [y / 4] + [3 * (M + 1) / 5] + d + 7 + i
//        //日的天
//        int gd = 4 * first_year + first_year / 4 + 5 * last_year + last_year / 4 + 3 * (month + 1) / 5 + day - 3;
//        //日的地
//        int i = month % 2 == 0 ? 6 : 0;
//        int zd = 8 * first_year + first_year / 4 + 5 * last_year + last_year / 4 + 3 * (month + 1) / 5 + day + 7 + i;
//        gd = (gd+1) %10;
//        zd = (zd+1) % 12;
//        String dayZG = gan[gd] + zhi[zd];//得到日的天干地支

        //年的天
        int year = getChineseDay(bean)[0] - 1900 + 36;
        int gy = year % 10;
        //年的地
        int zy = year % 12;
        String yearZG = gan[gy] + zhi[zy];//得到年的天干地支

//        int g = gy % 5;
//        String monthZG = gzmArr[getChineseDay(bean)[1] - 1][g];

        return yearZG + "年";//+ monthZG + "月" + dayZG + "日";
    }


    /**
     * 获取农历的具体日期
     */
    public static int[] getChineseDay(CalendarBean bean) {
        int year = bean.getYear();
        int month = bean.getMonth();
        int day = bean.getDay();
        int leapMonth;
        Date baseDate;
        Date currentDate;
        SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日");
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日");
            currentDate = chineseDateFormat.parse(year + "年" + month + "月" + day + "日");
        } catch (ParseException e) {
            return null;
        }

        // 求出和1900年1月31日相差的天数
        int offset = (int) ((currentDate.getTime() - baseDate.getTime()) / 86400000L);
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        // 农历年份
        int mLuchYear = iYear;

        leapMonth = (int) (LUNAR_INFO[iYear - 1900] & 0xf); // 闰哪个月,1-12
        // 用于标识是否为闰年
        boolean isLoap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !isLoap) {
                --iMonth;
                isLoap = true;
                daysOfMonth = leapDays(mLuchYear);
            } else
                daysOfMonth = monthDays(mLuchYear, iMonth);

            offset -= daysOfMonth;
            // 解除闰月
            if (isLoap && iMonth == (leapMonth + 1))
                isLoap = false;
            if (!isLoap) {
            }
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (isLoap) {
                isLoap = false;
            } else {
                isLoap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;

        }
        int mLuchMonth = iMonth;
        int mLuchDay = offset + 1;
        return new int[]{mLuchYear, mLuchMonth, mLuchDay};
    }

    /**
     * 传回农历 year年的总天数
     */
    private static int yearDays(int chinaYear) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[chinaYear - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(chinaYear));
    }

    /**
     * 传回农历 year年month月的总天数
     */
    private static int monthDays(int chinaYear, int chinaMonth) {
        if ((LUNAR_INFO[chinaYear - 1900] & (0x10000 >> chinaMonth)) == 0)
            return 29;
        else
            return 30;
    }

    /**
     * 传回农历 year年闰月的天数
     */
    private static int leapDays(int chinaYear) {
        //农历 year年闰哪个月 1-12 , 没闰传回 0
        int leapMonth = (int) (LUNAR_INFO[chinaYear - 1900] & 0xf);
        if (leapMonth != 0) {
            if ((LUNAR_INFO[chinaYear - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    private final static long[] LUNAR_INFO = new long[]{0x04bd8, 0x04ae0,
            0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
            0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
            0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
            0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
            0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
            0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
            0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
            0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
            0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
            0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
            0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
            0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
            0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
            0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
            0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
            0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
            0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
            0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
            0x0ada0};
    private static String[][] gzmArr = {{"丙寅", "戊寅", "庚寅", "壬寅", "甲寅"},
            {"丁卯", "己卯", "辛卯", "癸卯", "乙卯"},
            {"戊辰", "庚辰", "壬辰", "甲辰", "丙辰"},
            {"己巳", "辛巳", "癸巳", "乙巳", "丁巳"},
            {"庚午", "壬午", "甲午", "丙午", "戊午"},
            {"辛未", "癸未", "乙未", "丁未", "己未"},
            {"壬申", "甲申", "丙申", "戊申", "庚申"},
            {"癸酉", "乙酉", "丁酉", "己酉", "辛酉"},
            {"甲戌", "丙戌", "戊戌", "庚戌", "壬戌"},
            {"乙亥", "丁亥", "己亥", "辛亥", "癸亥"},
            {"丙子", "戊子", "庚子", "壬子", "甲子"},
            {"丁丑", "己丑", "辛丑", "癸丑", "乙丑"}};

}
