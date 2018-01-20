package com.ljy.ljyutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.util.LjySystemUtil;

public class BallActivity extends AppCompatActivity {

    private Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
    }

    public void onBallBtnClick(View view) {
        switch (view.getId()){
            case R.id.btn_fast:
                LjySystemUtil.addShortcut(activity, BallActivity.class.getName(),"小球快捷入口",R.drawable.ic_music);
                break;
        }
    }
}
