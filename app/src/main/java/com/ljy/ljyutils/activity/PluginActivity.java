package com.ljy.ljyutils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.plugincorelib.ProxyActivity;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyPermissionUtil;

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
        Intent intent=new Intent(mContext,ProxyActivity.class);
        intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, LjyFileUtil.getSDCardPath()+"LjyPlugin/plugin1.apk");
        startActivity(intent);
    }


}
