package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 侧滑菜单（抽屉）
 * <p>
 * 1.DrawerLayout
 * 使用时直接将DrawerLayout作为根布局，然后其内部第一个View为内容区域，第二个View为左侧菜单，第三个View为右侧侧滑菜单，
 * 当然第三个是可选的。主内容区的布局代码要放在侧滑菜单布局的前面,这样可以帮助DrawerLayout判断谁是侧滑菜单，谁是主内容区；
 * 第一个View也即主界面的宽高应当设置为match_parent。
 * 第二、三个View需要设置android:layout_gravity="left"，和android:layout_gravity="right"且一般高度设置为match_parent，宽度为固定值，即侧滑菜单的宽度。
 * 按照上面的描述写个布局文件，然后设置给Activity就添加好了左右侧滑
 * <p>
 * //LOCK_MODE_UNDEFINED:默认未定义
 * //LOCK_MODE_UNLOCKED:抽屉是没有锁的
 * //LOCK_MODE_LOCKED_CLOSED:抽屉锁是关闭的，用户不能打开它，但是可以通过代码的方式来打开它
 * //LOCK_MODE_LOCKED_OPEN:抽屉是锁是打开的，不能关闭它，但是可以通过代码的方式关闭它。
 */
public class DrawerActivity extends BaseActivity {


    @BindView(R.id.drawer_layout_root)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(mActivity);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
    }

    public void openMenu(View view) {
        switch (view.getId()) {
            case R.id.btn_main_left:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.btn_main_right:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            default:
                break;
        }

    }
}
