package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyColorUtil;
import com.ljy.util.LjyViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSizeActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_size);
        ButterKnife.bind(mActivity);
        initView();
    }


    private void initView() {
        for (int i = 0; i < 5; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8, 8, 8, 8);
            TextView view = new TextView(mContext);
            view.setBackgroundColor(LjyColorUtil.getInstance().randomColor());
            view.setText("--->"+i);
            view.setTextColor(getResources().getColor(R.color.white));
            LjyViewUtil.setViewSize(mContext, layoutParams, 0.5f, 0.5f, i);
            view.setLayoutParams(layoutParams);
            lin.addView(view);
        }
    }
}
