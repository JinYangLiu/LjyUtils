package com.ljy.util;

import java.util.Random;

/**
 * Created by Mr.LJY on 2018/1/3.
 */

public class LjyColorUtil {
    private static final LjyColorUtil ourInstance = new LjyColorUtil();
    private final Random random;

    public static LjyColorUtil getInstance() {
        return ourInstance;
    }

    private LjyColorUtil() {
        random = new Random();
    }

    public int randomColor() {
        return 0xff000000 | random.nextInt(0x00ffffff);
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int alpha(int color) {
        return (color >> 24) & 0xFF;
    }
}
