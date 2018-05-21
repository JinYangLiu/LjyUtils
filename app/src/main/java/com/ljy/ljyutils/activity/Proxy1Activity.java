package com.ljy.ljyutils.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class Proxy1Activity extends BaseActivity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;

    public static final String FROM="extra_from";
    public static final int FROM_EXTERNAL=0;
    public static final int FROM_INTERNAL=1;
    public static final String EXTRA_DEX_PATH="extra_dex_path";
    public static final String EXTRA_CLASS="extra_class";
    private String mDexPath;
    private String mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy1);
        mDexPath=getIntent().getStringExtra(EXTRA_DEX_PATH);
        File apkFile = new File(mDexPath);
        LjyLogUtil.i("mDexPath:"+mDexPath);
        LjyLogUtil.i("apkFile:"+apkFile.exists());
        mClass=getIntent().getStringExtra(EXTRA_CLASS);
        if (mClass==null){
            launchTargetActivity();
        }else{
            launchTargetActivity(mClass);
        }
    }

    private void launchTargetActivity() {
        PackageInfo packageInfo=getPackageManager()
                .getPackageArchiveInfo(mDexPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo.activities!=null
                && packageInfo.activities.length>0){
            String activityName=packageInfo.activities[0].name;
            mClass=activityName;
            launchTargetActivity(mClass);
        }
    }

    private void launchTargetActivity(String className) {
        File dexOutputDir=getDir("dex",0);
        String dexOutputPath=dexOutputDir.getAbsolutePath();
        ClassLoader localClassLoader=ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader=new DexClassLoader(mDexPath,dexOutputPath,null,localClassLoader);
        try {
            Class localClass=dexClassLoader.loadClass(className);
            Constructor localConstructor=localClass.getConstructor(new Class[]{});
            Object instance=localConstructor.newInstance(new Object[]{});

            Method setProxy=localClass.getMethod("setProxy",
                    new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(instance,new Object[]{this});

            Method onCreate=localClass.getDeclaredMethod("onCreate",
                    new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            Bundle bundle=new Bundle();
            bundle.putInt(FROM,FROM_EXTERNAL);
            onCreate.invoke(instance,new Object[]{bundle});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    //加载插件中的资源
    private void loadResources() {
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method addAssetPath=assetManager.getClass().
                    getMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,mDexPath);
            mAssetManager=assetManager;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Resources superRes=super.getResources();
        mResources=new Resources(mAssetManager,superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme=mResources.newTheme();
        mTheme.setTo(super.getTheme());


    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager==null?super.getAssets():mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources==null?super.getResources():mResources;
    }

}
