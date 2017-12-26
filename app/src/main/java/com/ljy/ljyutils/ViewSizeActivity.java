package com.ljy.ljyutils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljy.lib.LjyViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSizeActivity extends AppCompatActivity {

    @BindView(R.id.linear)
    LinearLayout lin;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_size);
        ButterKnife.bind(this);
        initView();
    }

    int[] colorArr = {R.color.red, R.color.green, R.color.blue, R.color.gray, R.color.black,};

    private void initView() {
        for (int i = 0; i < 5; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8, 8, 8, 8);
            TextView view = new TextView(mContext);
            view.setBackgroundColor(getResources().getColor(colorArr[i]));
            view.setText("--->"+i);
            view.setTextColor(getResources().getColor(R.color.white));
            LjyViewUtil.setViewSize(mContext, layoutParams, 0.5f, 0.5f, i);
            view.setLayoutParams(layoutParams);
            lin.addView(view);
        }
    }
}
