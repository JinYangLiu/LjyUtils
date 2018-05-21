package com.ljy.demoplugin1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;

/**
 * Created by ljy on 2018/5/21.
 */

public class BaseActivity extends Activity {
    protected Activity mProxyActivity=this;
    public static final String FROM="extra_from";
    public static final int FROM_EXTERNAL=0;
    public static final int FROM_INTERNAL=1;
    public static final String EXTRA_DEX_PATH="extra_dex_path";
    public static final String EXTRA_CLASS="extra_class";
    private int mFrom=FROM_INTERNAL;
    private String PROXY_VIEW_ACTION="com.ljy.Action.Proxy1Activity";
    private String DEX_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator+"LjyPlugin/plugin1.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            mFrom=savedInstanceState.getInt(FROM,FROM_INTERNAL);
        }
        if (mFrom==FROM_INTERNAL){
            super.onCreate(savedInstanceState);
            mProxyActivity=this;
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity==this){
            super.setContentView(view);
        }else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity==this){
            super.setContentView(view, params);
        }else {
            mProxyActivity.setContentView(view,params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity==this){
            super.setContentView(layoutResID);
        }else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mProxyActivity==this) {
            return super.findViewById(id);
        }else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Resources getResources() {
        if (mProxyActivity==this) {
            return super.getResources();
        } else {
            return mProxyActivity.getResources();
        }
    }

    @Override
    public AssetManager getAssets() {
        if (mProxyActivity==this) {
            return super.getAssets();
        }else {
            return mProxyActivity.getAssets();
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity==this){
            super.addContentView(view, params);
        }else {
            mProxyActivity.addContentView(view, params);
        }
    }

    public void setProxy(Activity proxyActivity){
        mProxyActivity=proxyActivity;
    }

    protected void startActivityByProxy(String className){
        Log.i("ljy",DEX_PATH);
        if (mProxyActivity==this){
            Intent intent=new Intent();
            intent.setClassName(this,className);
            this.startActivity(intent);
        }else {
            //通过隐式Intent跳转Activity
            Intent intent=new Intent(PROXY_VIEW_ACTION);
            intent.putExtra(EXTRA_DEX_PATH,DEX_PATH);
            intent.putExtra(EXTRA_CLASS,className);
            mProxyActivity.startActivity(intent);
        }
    }

}
