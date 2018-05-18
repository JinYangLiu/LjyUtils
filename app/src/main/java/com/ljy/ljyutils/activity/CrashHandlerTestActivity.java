package com.ljy.ljyutils.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ljy.ljyutils.R;

public class CrashHandlerTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_handler_test);
    }

    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_crash:
                throw new RuntimeException("自定义异常：这是自己抛出的异常，用于测试crashHandler");
        }
    }
}
