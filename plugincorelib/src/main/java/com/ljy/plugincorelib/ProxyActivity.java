package com.ljy.plugincorelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class ProxyActivity extends Activity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;

    public static final String FROM = "extra_from";
    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;
    public static final String EXTRA_DEX_PATH = "extra_dex_path";
    public static final String EXTRA_CLASS = "extra_class";
    private String mDexPath;
    private String mClass;
    private HashMap<String, Method> mActivityLifecircleMethods = new HashMap();
    private Object mRemoteInstance;
    private ActivityLifeInterface mActLife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        loadResources(mDexPath);
        File apkFile = new File(mDexPath);
        if (!apkFile.exists()) {
            Toast.makeText(this, "请先下载插件到如下路径：" + mDexPath, Toast.LENGTH_SHORT).show();
            return;
        }
        mClass = getIntent().getStringExtra(EXTRA_CLASS);
        if (mClass == null) {
            launchTargetActivity();
        } else {
            launchTargetActivity(mClass);
        }
    }

    private void launchTargetActivity() {
        PackageInfo packageInfo = getPackageManager()
                .getPackageArchiveInfo(mDexPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo.activities != null
                && packageInfo.activities.length > 0) {
            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            launchTargetActivity(mClass);
        }
    }

    private void launchTargetActivity(String className) {
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        //下面一行之前是用ClassLoader.getSystemClassLoader();
        // 但是 (ActivityLifeInterface) mRemoteInstance会报类型转化错误
        ClassLoader localClassLoader = getApplicationContext().getClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath, dexOutputPath, null, localClassLoader);
        try {
            Class localClass = dexClassLoader.loadClass(className);
            Constructor localConstructor = localClass.getConstructor(new Class[]{});
            mRemoteInstance = localConstructor.newInstance(new Object[]{});

            mActLife = (ActivityLifeInterface) mRemoteInstance;

//            instsantiateLifecircleMethods((Class<Activity>) localClass);

            Method setProxy = localClass.getMethod("setProxy",
                    new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(mRemoteInstance, new Object[]{this});

            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_EXTERNAL);
            Method onCreate = localClass.getDeclaredMethod("onCreate",
                    new Class[]{Bundle.class});
            onCreate.setAccessible(true);
//            Method onCreate=mActivityLifecircleMethods.get("onCreate");
            onCreate.invoke(mRemoteInstance, new Object[]{bundle});

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
    private void loadResources(String mDexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().
                    getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());


    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    /**
     * 通过反射管理插件Activity的生命周期
     * 但是经常出现 java.lang.NoSuchMethodException: onDestroy [] 这种错误
     * 上述错误出现原因是 localClass 中没有重写相应方法，重写了就不会出现了
     *
     * @param localClass
     */
    private void instsantiateLifecircleMethods(Class localClass) {
        String[] methodNames = new String[]{
                "onRestart",
                "onStart",
                "onResume",
                "onPause",
                "onStop",
                "onDestroy"
        };

        for (String methodName : methodNames) {
            try {
                Method method = localClass.getDeclaredMethod(methodName);
                method.setAccessible(true);
                mActivityLifecircleMethods.put(methodName, method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }

        try {

            Method onCreate = localClass.getDeclaredMethod("onCreate", new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            mActivityLifecircleMethods.put("onCreate", onCreate);

            Method onActivityResult = localClass.getDeclaredMethod("onActivityResult",
                    new Class[]{int.class, int.class, Intent.class});
            onActivityResult.setAccessible(true);
            mActivityLifecircleMethods.put("onActivityResult", onActivityResult);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActLife.onResume();
//        Method method=mActivityLifecircleMethods.get("onResume");
//        if (method!=null){
//            try {
//                method.invoke(mRemoteInstance);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActLife.onPause();
//        Method method=mActivityLifecircleMethods.get("onPause");
//        if (method!=null){
//            try {
//                method.invoke(mRemoteInstance);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
    }


}
