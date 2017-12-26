package com.ljy.ljyutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljy.lib.LjyLogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LjyLogUtil.i("abc----");
    }
}
