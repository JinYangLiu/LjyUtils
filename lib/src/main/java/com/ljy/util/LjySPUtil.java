package com.ljy.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mr.LJY on 2017/12/26.
 * <p>
 * SharedPreferences工具类
 */

public class LjySPUtil {

    private final static String SP_NAME = "LJY_SP_NAME";
    private SharedPreferences sp;

    public LjySPUtil(Context context) {
        if (sp == null) {
            context = context.getApplicationContext();
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            /**
             * 第二个参数指定文件的操作模式，共有四种操作模式:
             1. Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，
             写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
             2. Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
             3,4. Context.MODE_WORLD_READABLEContext.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
             a. MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；
             b. MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
             */
        }
    }

    public void save(String key, boolean value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public void save(String key, int value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public void save(String key, long value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public void save(String key, float value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public void save(String key, String value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void save(String key, Set<String> value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet(key, value);
        edit.apply();
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清空到配置文件
     */
    public void clearAll() {
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }

    /**
     * 从配置文件中读取
     */
    public String get(String tag, String defValue) {
        return sp.getString(tag, defValue);
    }

    public Set<String> get(String tag, Set<String> defValue) {
        return sp.getStringSet(tag, defValue);
    }

    public boolean get(String tag, boolean defValue) {
        return sp.getBoolean(tag, defValue);
    }

    public int get(String tag, int defValue) {
        return sp.getInt(tag, defValue);
    }

    public float get(String tag, float defValue) {
        return sp.getFloat(tag, defValue);
    }

    public long get(String tag, long defValue) {
        return sp.getLong(tag, defValue);
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

}
