package com.ljy.lib;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Mr.LJY on 2017/12/25.
 */

public class LjyStringUtil {
    /**
     * 判断字符串是否纯数字
     *
     * @param s
     * @return
     */
    public final static boolean isNumber(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }

    /**
     * 使用BigDecimal进行精确运算
     * 保留len位小数
     *
     * 1 public BigDecimal add(BigDecimal value);      //加法
     * 2 public BigDecimal subtract(BigDecimal value); //减法
     * 3 public BigDecimal multiply(BigDecimal value); //乘法
     * 4 public BigDecimal divide(BigDecimal value);   //除法
     *
     * @param num
     * @param len
     * @return
     */
    public static String keepAfterPoint(BigDecimal num, int len) {
        len = len > 0 ? len : 1;
        StringBuffer pattern = new StringBuffer();
        pattern.append("0.");
        for (int i = 0; i < len; i++) {
            pattern.append("0");
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());
        String temp = df.format(num.doubleValue());
        return temp;
    }
}
