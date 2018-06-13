package com.ljy.ljyutils.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljy.ljyutils.R;
import com.ljy.view.LjyBigImageView;

import java.io.IOException;
import java.io.InputStream;

public class BigImgActivity extends AppCompatActivity {

    private LjyBigImageView bigImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_img);
        bigImgView = findViewById(R.id.bigImgView);
        try {
            InputStream inputStream = getResources().getAssets().open("world.jpg");
            bigImgView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
