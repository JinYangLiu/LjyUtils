package com.ljy.view.swipeBack;

/**
 * Created by Mr.LJY on 2018/1/31.
 */

public interface SwipeBackActivityBase {
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean var1);

    void scrollToFinishActivity();
}
