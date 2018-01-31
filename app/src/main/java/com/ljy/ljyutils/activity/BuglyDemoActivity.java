package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuglyDemoActivity extends BaseActivity {

    @BindView(R.id.btnShowToast)
    Button showT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bugly_demo);
        ButterKnife.bind(mActivity);
        showT.setTextColor(0xffff0000);
    }

    public void onBuglyBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowToast:  // 测试热更新功能
                Toast.makeText(mContext, "fixed", Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, "bug:"+(1/0), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnKillSelf: // 杀死进程
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.btnCheckUpgrade://检查更新
                Beta.checkUpgrade();
                break;
        }
    }
}
