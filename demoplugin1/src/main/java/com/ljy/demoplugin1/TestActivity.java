package com.ljy.demoplugin1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView textView=findViewById(R.id.textView);
        textView.setText("这是插件的测试页面");
        ImageView imageView=findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.test));

    }
}
