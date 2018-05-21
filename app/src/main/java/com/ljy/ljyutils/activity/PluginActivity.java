package com.ljy.ljyutils.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.BaseAdapter;

import com.ljy.ljyutils.Manifest;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyPermissionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Permission;

public class PluginActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }

    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_demo1:
                if (checkPremission(android.Manifest.permission.READ_EXTERNAL_STORAGE,222))
                    toPluginDemo1();
                break;
        }
    }

    @LjyPermissionUtil.GetPermission(permissionResult = true, requestCode = 222)
    private void toPluginDemo1() {
        Intent intent=new Intent(mContext,Proxy1Activity.class);
        intent.putExtra(Proxy1Activity.EXTRA_DEX_PATH, LjyFileUtil.getSDCardPath()+"LjyPlugin/plugin1.apk");
        startActivity(intent);
    }


}
