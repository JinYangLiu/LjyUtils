package com.ljy.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;

/**
 * Created by Mr.LJY on 2017/12/25.
 * <p>
 * 字符串操作的工具类
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
     * <p>
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

    /**
     * byte[]转Hex(16进制)字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        for (int n = 0; n < b.length; n++) {
            String stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0" + stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * Hex(16进制)字符串 转 byte[]
     *
     * @param strhex
     * @return
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
//        如果row是负奇数，那么row % 2 == -1，
//        解决方法：
//        考虑使用x & 1 == 1或者x % 2 != 0
        if (l % 2 != 0) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    /**
     * byte[]转Base64字符串
     *
     * @param val
     * @return
     */
    public static String byte2base64(byte[] val) {
        return Base64.encodeToString(val, Base64.DEFAULT);
    }

    /**
     * Base64字符串 转 byte[]
     *
     * @param val
     * @return
     */
    public static byte[] base642byte(String val) {
        return Base64.decode(val, Base64.DEFAULT);
    }

    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /**
     * 生成随机数，可以当做动态的密钥
     */
    public static String getRandomStr(int len) {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[len];
            localSecureRandom.nextBytes(bytes_key);
            return byte2hex(bytes_key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 2进制转字符串
     */
    public static String binaryToString(String binStr) {
        String[] tempStr = binStr.split(" ");
        byte[] bytes = new byte[tempStr.length];
        for (int i = 0; i < tempStr.length; i++) {
            bytes[i] = Long.valueOf(tempStr[i], 2).byteValue();
        }
        try {
            return new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符串转2进制
     */
    public static String stringToBinary(String info) {
        byte[] strChar = new byte[0];
        try {
            strChar = info.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteToBinary(strChar);
    }

    /**
     * byte数组转换为二进制字符串
     **/
    private static String byteToBinary(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String bStr = Long.toString(b[i] & 0xff, 2);
            while (bStr.length() <8) {
                bStr = "0" + bStr;
            }
            result.append(bStr);
        }
        return result.toString();
    }

}
