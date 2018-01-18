package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ljy.ljyutils.R;

/**
 * Lottie是Airbnb开源的一个支持 Android、iOS 以及 ReactNative，利用json文件的方式快速实现动画效果的库。
 */
public class LottieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
    }
}
