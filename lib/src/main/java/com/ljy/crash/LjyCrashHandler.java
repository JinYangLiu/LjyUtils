package com.ljy.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyTimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ljy on 2018/5/18.
 */

public class LjyCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final LjyCrashHandler ourInstance = new LjyCrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashhandler;
    private Context mContext;
    private String CRASH_LOG_PATH =Environment.getExternalStorageDirectory().getPath()+"/LjyUtils/log/";
    private String CRASH_LOG_NAME="crash_";
    private String CRASH_FILE_NAME_SUFFIX=".txt";

    public static LjyCrashHandler getInstance() {
        return ourInstance;
    }

    private LjyCrashHandler() {
    }

    public void init(Context context){
        mContext=context.getApplicationContext();
        mDefaultCrashhandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 核心方法，当系统有未被捕获的异常就会自动调用uncaughtException方法
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            //导出异常信息到sd卡
            dumpExceptionToSDCard(e);
            //上传异常信息到服务器
            uploadExceptionToServer(e);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();
        //如果系统提供来默认的异常处理器，则交给系统去结束程序，否则自己结束自己
        if (mDefaultCrashhandler!=null){
            mDefaultCrashhandler.uncaughtException(t, e);
        }else {
            Process.killProcess(Process.myPid());
        }

    }

    private void uploadExceptionToServer(Throwable ex) {
        // TODO upload exception message to your web server

    }

    private void dumpExceptionToSDCard(Throwable ex)throws IOException{
        //如果sd卡不存在或无法使用，则无法把异常信息写入sd卡
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            LjyLogUtil.w("sdcard unmounted ,skip dump exception.");
            return;
        }
        File dir=new File(CRASH_LOG_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        long current=System.currentTimeMillis();
        String time= LjyTimeUtil.timestampToDate(current);
        File file=new File(CRASH_LOG_PATH +CRASH_LOG_NAME+time+CRASH_FILE_NAME_SUFFIX);
        try {
            PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            LjyLogUtil.e("dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm=mContext.getPackageManager();
        PackageInfo pi=pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        //app版本
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        //android系统版本
        pw.print("os Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("model: ");
        pw.println(Build.MODEL);
        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

    }


}
