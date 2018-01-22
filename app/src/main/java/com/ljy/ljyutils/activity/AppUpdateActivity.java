package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjySystemUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppUpdateActivity extends BaseActivity {

    @BindView(R.id.text_info)
    TextView textInfo;

    String updateAppPath = "http://m.anxin.com/down/down.html?cmd=anxinapk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);
        ButterKnife.bind(mActivity);
        initView();
    }

    private void initView() {
        String info = String.format("packageName:%s\nappName:%s\nversionName:%s\nversionCode:%d\n",
                getPackageName(), LjySystemUtil.getAppName(mContext), LjySystemUtil.getVersionName(mContext), LjySystemUtil.getVersionCode(mContext));

        textInfo.setText(info);
    }

    public void onUpdateBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_app:
                if (LjySystemUtil.hasPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    updateApp();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
                }
                break;
            default:
                break;
        }
    }

    private void updateApp() {


    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LjySystemUtil.onPermissionResult(grantResults, new LjySystemUtil.PermissionResult() {
            @Override
            public void success() {
                if (requestCode == 999) {
                    updateApp();
                }
            }

            @Override
            public void fail(List<Integer> disAllowIndexs) {
                for (int index : disAllowIndexs) {
                    LjyLogUtil.i(String.format("%s 权限被拒绝", permissions[index]));
                }
            }
        });
    }
}
