package com.ljy.javacode.util;

/**
 * Created by LJY on 2018/6/11 13:42
 */
public class HashUtil {
    private static final HashUtil ourInstance = new HashUtil();

    public static HashUtil getInstance() {
        return ourInstance;
    }

    private HashUtil() {
    }

    //根据 关键字值 计算 哈希值
    public int hashInt(int key, int arraySize) {
        int hashVal = key % arraySize;
        System.out.println("hashFunc: arraySize:" + arraySize + ", key:" + key + ", hashVal:" + hashVal);
        return hashVal;
    }

    // 再哈希法计算探测序列
    public int hashInt2(int key, int arraySize) {
        int hashVal = 5 - key % 5;
        System.out.println("hashFunc_2: arraySize:" + arraySize + ", key:" + key + ", hashVal:" + hashVal);
        return hashVal;
    }

    public int hashStr(String key, int arraySize) {
        int hashVal = 0;
        int pow27 = 1;
        for (int i = key.length() - 1; i >= 0; i--) {
            int letter = key.charAt(i) - 96;
            hashVal += pow27 * letter;
            pow27 *= 27;
        }
        return hashVal % arraySize;
    }

    //不能处理大于7个字符的字符串,否则hashVal超出int范围
    public int hashStrHorner(String key, int arraySize) {
        int hashVal = key.charAt(0) - 96;
        for (int i = 1; i < key.length(); i++) {
            int letter = key.charAt(i) - 96;
            hashVal = hashVal * 27 + letter;
        }
        return hashVal % arraySize;
    }

    //修正hashStrHorner的不足
    public int hashStrHorner2(String key, int arraySize) {
        int hashVal = 0;
        for (int i = 1; i < key.length(); i++) {
            int letter = key.charAt(i) - 96;
            hashVal = (hashVal * 27 + letter) % arraySize;
        }
        return hashVal;
    }
}
