package com.ljy.demoplugin1;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.plugincorelib.BaseActivity;

public class TestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView textView=findViewById(R.id.textView);
        textView.setText("这是插件的测试页面");
        ImageView imageView=findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.test));

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(mProxyActivity,"插件TestActivity.onResume",Toast.LENGTH_SHORT).show();
        Log.i("ljy","插件TestActivity.onResume");
    }
}
