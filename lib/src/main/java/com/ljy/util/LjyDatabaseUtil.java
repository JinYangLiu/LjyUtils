package com.ljy.util;

import android.content.Context;

import com.ljy.util.db.MyOpenHelper;

/**
 * Created by Mr.LJY on 2018/4/8.
 */

public class LjyDatabaseUtil {
    private static MyOpenHelper mHelper;
    /**
     * 一般来说这里的initHelper放到application中去初始化
     * 当然也可以在项目运行阶段初始化
     */
    public static void initHelper(Context context, String name){
        if(mHelper == null){
            mHelper = new MyOpenHelper(context,name);
        }
    }
    public static MyOpenHelper getHelper(){
        if(mHelper == null){
            new RuntimeException("MyOpenHelper is null,No init it");
        }
        return mHelper;
    }
}
