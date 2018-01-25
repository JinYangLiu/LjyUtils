package com.ljy.ljyutils.activity;

import android.os.Bundle;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.view.LjyArgueProgressView;
import com.ljy.view.LjyZanView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArgueProgressActivity extends BaseActivity {

    @BindView(R.id.argueProgressView)
    LjyArgueProgressView mLjyArgueProgressView;
    @BindView(R.id.ljyZanView)
    LjyZanView ljyZanView;
    private int a = 1;
    private int b = 9;
    private String str1 = "<font color=\"#d50000\">" + "【正方】" + "</font>" + "Android牛";
    private String str2 = "<font color=\"#1976d2\">" + "【反方】" + "</font>" + "iOS牛";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argue_progress);
        ButterKnife.bind(mActivity);
        initView();
        ljyZanView.setNum(a, b);
        ljyZanView.setOnClickCallBack(new LjyZanView.OnClickCallBack() {
            @Override
            public void listenerLike() {
                ljyZanView.setNum(++a, b);
                mLjyArgueProgressView.setUI(a, b, str1, str2);
            }

            @Override
            public void listenerDisLike() {
                ljyZanView.setNum(a, ++b);
                mLjyArgueProgressView.setUI(a, b, str1, str2);
            }
        });
    }

    private void initView() {
        mLjyArgueProgressView.setUI(a, b, str1, str2);
        mLjyArgueProgressView.setOnClickListener(new LjyArgueProgressView.OnClickListener() {
            @Override
            public boolean listenerYes() {
                mLjyArgueProgressView.setUI(++a, b, str1, str2);
                mLjyArgueProgressView.btnClickable(true);
                return true;
            }

            @Override
            public boolean listenerNo() {
                mLjyArgueProgressView.setUI(a, ++b, str1, str2);
                mLjyArgueProgressView.btnClickable(true);
                return true;
            }
        });

    }
}
