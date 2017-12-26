package com.ljy.ljyutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMainBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_app_util:
                startActivity(new Intent(mContext, UseUtilsActivity.class));
                break;
            case R.id.btn_glide_util:
                startActivity(new Intent(mContext, GlideUtilActivity.class));
                break;
            case R.id.btn_setViewSize:
                startActivity(new Intent(mContext, ViewSizeActivity.class));
                break;
            default:
                break;
        }
    }
}
