package com.ljy.ljyutils.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.User;
import com.ljy.ljyutils.databinding.ActivityBindBinding;

/**
 * Android官方数据绑定框架DataBinding
 * 1.
 * android {
 * ...
 * <p>
 * //导入dataBinding支持
 * dataBinding{
 * enabled true
 * }
 * <p>
 * ...
 * }
 * 2. layout布局中写标签
 * <p>
 * 3.绑定数据
 */
public class BindDataActivity extends BaseActivity {

    private ActivityBindBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_bind);
        User mUser = new User("小明", "ax123123", "19");
        mBinding.setUser(mUser);
    }
}
