package com.ljy.demoplugin1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
        //访问不到资源，所以通过代码布局
        //访问不到资源，所以通过代码布局
        LinearLayout layout=new LinearLayout(mProxyActivity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.YELLOW);
        Button button=new Button(mProxyActivity);
        button.setText("这是插件的测试页面");
        layout.addView(button, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(layout);
    }
}
