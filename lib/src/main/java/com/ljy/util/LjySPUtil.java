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

    private final String SP_NAME = "LJY_SP_NAME";
    private SharedPreferences sp;

    public LjySPUtil(Context context) {
        if (sp == null) {
            context = context.getApplicationContext();
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
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

}
