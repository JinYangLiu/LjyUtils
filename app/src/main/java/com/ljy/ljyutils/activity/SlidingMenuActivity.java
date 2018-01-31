package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjySystemUtil;
import com.ljy.view.slidingMenu.LjySlidingMenu;

/**
 * 所谓工欲善其事，必先利其器，我们先了解下SlidingMenu 的一些常用属性和方法
 * <p>
 * menu.setMode(SlidingMenu.LEFT_RIGHT);//设置侧滑菜单的位置，可选值LEFT , RIGHT , LEFT_RIGHT （两边都有菜单时设置）
 * <p>
 * menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 设置触摸屏幕的模式，可选值MARGIN , CONTENT
 * <p>
 * menu.setShadowWidthRes(R.dimen.shadow_width);//根据dimension资源文件的ID来设置阴影的宽度
 * <p>
 * menu.setShadowDrawable(R.drawable.shadow);//根据资源文件ID来设置滑动菜单的阴影效果
 * <p>
 * menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
 * <p>
 * menu.setBehindWidth(400);//设置SlidingMenu菜单的宽度
 * <p>
 * menu.setFadeDegree(0.35f);//SlidingMenu滑动时渐入渐出效果的值
 * <p>
 * menu.setBehindScrollScale(1.0f);//设置SlidingMenu与下方视图的移动的速度比，当为1时同时移动，取值0-1
 * <p>
 * menu.setSecondaryShadowDrawable(R.drawable.shadow);//设置二级菜单的阴影效果
 * <p>
 * menu.setSecondaryMenu(R.layout.right_menu_frame);//设置右边（二级）侧滑菜单
 * <p>
 * menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
 * <p>
 * menu.setMenu(R.layout.menu_layout);//设置menu的布局文件
 * <p>
 * menu.toggle();//动态判断自动关闭或开启SlidingMenu
 * <p>
 * menu.showMenu();//显示SlidingMenu
 * <p>
 * menu.showContent();//显示内容
 */
public class SlidingMenuActivity extends BaseActivity {

    private LjySlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);
        initSlidingMenu();
    }

    private void initSlidingMenu() {
        // configure the SlidingMenu
        mSlidingMenu = new LjySlidingMenu(this);
        mSlidingMenu.setMode(LjySlidingMenu.RIGHT);
        // 设置触摸屏幕的模式
        mSlidingMenu.setTouchModeAbove(LjySlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setShadowWidth(LjySystemUtil.dp2px(mContext, 20));
//        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        mSlidingMenu.setBehindOffset(LjySystemUtil.dp2px(mContext, 200));
        // 设置渐入渐出效果的值
        mSlidingMenu.setFadeDegree(0.35f);
        //绑定到哪一个Activity对象
        mSlidingMenu.attachToActivity(mActivity, LjySlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        mSlidingMenu.setMenu(R.layout.sliding_right_menu);
    }

    public void onSlidingMenuBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_showMenu:
                mSlidingMenu.showMenu();
                break;
            case R.id.btn_hideMenu:
                mSlidingMenu.showContent();
                break;
            case R.id.btn_toggleMenu:
                mSlidingMenu.toggle(true);
                break;
            default:
                break;
        }
    }
}
