package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyGestureLockView;
import com.ljy.view.LjyPwdInputView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GestureLockActivity extends BaseActivity {

    @BindView(R.id.gestureLockView)
    LjyGestureLockView mGestureLockView;
    @BindView(R.id.passwordInputView)
    LjyPwdInputView mPwdInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        ButterKnife.bind(mActivity);
        initView();
    }

    private String inputPwd = "123456";
    private String gesturePwd = "#123654";

    private void initView() {
        mPwdInputView.addTextChangedListener(mTextWatcher);

        mGestureLockView.setOnPatterChangeListener(new LjyGestureLockView.OnPatterChangeListener() {
            @Override
            public void onPatterChange(String passwordStr) {
                LjyLogUtil.i("passwordStr:" + passwordStr);
                if (TextUtils.isEmpty(passwordStr) || passwordStr.length() < 5) {
                    LjyToastUtil.toast(mContext, "至少4个点");
                } else if (gesturePwd.equals(passwordStr)) {
                    LjyToastUtil.toast(mContext, "success");
                } else {
                    LjyToastUtil.toast(mContext, "error,密码：123654");
                    mGestureLockView.errorState();
                }
                mGestureLockView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGestureLockView.resetPoint();
                    }
                }, 375);
            }

            @Override
            public void onPatterStart(boolean isStart) {
                LjyLogUtil.i("isStart:" + isStart);
            }
        });
    }

    public void onGestureClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                mGestureLockView.resetPoint();
                break;
            case R.id.btn_2:
                mGestureLockView.errorState();
                break;
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence passwordStr;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passwordStr = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(passwordStr) && passwordStr.length() == 6) {
                if (inputPwd.equals(passwordStr.toString())) {
                    LjyToastUtil.toast(mContext, "true");
                } else {
                    LjyToastUtil.toast(mContext, "false,密码：123456");
                }
                mPwdInputView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPwdInputView.setText("");
                    }
                }, 375);
            }
        }
    };
}
