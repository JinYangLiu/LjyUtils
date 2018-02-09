package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjySystemUtil;

public class BallActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
    }

    public void onBallBtnClick(View view) {
        switch (view.getId()){
            case R.id.btn_fast:
                LjySystemUtil.addShortcut(mContext, BallActivity.class.getName(),"小球快捷入口",R.drawable.ic_music);
                break;
            default:
                break;
        }
    }
}
