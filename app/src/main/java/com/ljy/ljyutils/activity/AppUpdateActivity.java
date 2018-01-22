package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjySystemUtil;

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
                updateApp();
                break;
            default:
                break;
        }
    }

    private void updateApp() {

    }
}
