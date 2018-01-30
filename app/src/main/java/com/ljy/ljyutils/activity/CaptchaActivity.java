package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyCaptchaNumUtil;
import com.ljy.util.LjyToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptchaActivity extends BaseActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;

    @BindView(R.id.textView1)
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
        ButterKnife.bind(mActivity);

    }

    public void onCaptchaBtnClick(View view) {
        switch (view.getId()) {
            case R.id.button_reset:
                imageView1.setImageBitmap(LjyCaptchaNumUtil.getInstance().createBitmap());
                textView1.setText("验证码为："+ LjyCaptchaNumUtil.getInstance().getCode());
                break;
            default:
                break;
        }
    }
}
