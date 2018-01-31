package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyCaptchaNumUtil;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.captchaPic.LjyCaptchaPictureView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptchaActivity extends BaseActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;

    @BindView(R.id.textView1)
    TextView textView1;

    @BindView(R.id.captCha)
    LjyCaptchaPictureView captCha;

    @BindView(R.id.checkbox_captcha)
    CheckBox checkboxCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
        ButterKnife.bind(mActivity);

        initView();

    }

    private void initView() {
        captCha.setCaptchaListener(new LjyCaptchaPictureView.CaptchaListener() {
            @Override
            public void onAccess(long time) {
                LjyToastUtil.toast(mContext, "验证成功");
            }

            @Override
            public void onFailed(int count) {
                LjyToastUtil.toast(mContext, "验证失败,失败次数" + count);

            }

            @Override
            public void onMaxFailed() {
                LjyToastUtil.toast(mContext, "验证超过次数，你的帐号被封锁");
            }

        });

        captCha.setMode(LjyCaptchaPictureView.MODE_BAR );
        captCha.setIsShowText(true);
        checkboxCaptcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int modeType = isChecked ? LjyCaptchaPictureView.MODE_BAR : LjyCaptchaPictureView.MODE_NONBAR;
                captCha.setMode(modeType);
            }
        });
    }

    public void onCaptchaBtnClick(View view) {
        switch (view.getId()) {
            case R.id.button_reset:
                imageView1.setImageBitmap(LjyCaptchaNumUtil.getInstance().createBitmap());
                textView1.setText("验证码为：" + LjyCaptchaNumUtil.getInstance().getCode());
                captCha.reset(true,true);
                break;

            default:
                break;
        }
    }
}
