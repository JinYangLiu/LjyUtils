package com.ljy.ljyutils.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.view.CustomView;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewActivity extends BaseActivity {

    @BindView(R.id.customView)
    CustomView customView;
    @BindView(R.id.view)
    Button mView;
    @BindView(R.id.relativeBlue)
    RelativeLayout relativeBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

//        LjyScreenUtils.noStatusBar(mActivity);
//        LjyScreenUtils.noNavigationBar(mActivity);

        ButterKnife.bind(mActivity);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LjyLogUtil.i("mView onClick");
            }
        });
        LjyViewUtil.touchMove(mView);
    }

    private final int count = LjyDensityUtil.dp2px(mContext, 50);

    /**
     * View移动方式的总结
     * 1. scroller.startScroll,View.scrollTo,View.scrollBy
     * (都是对View内容的移动,而view本身并不动,从本例子的View背景并不动就可以看出)
     * 操作简单,适合对VIew内容的滑动
     * 2.动画
     * 操作简单,适用于没有交互的View和实现复杂的动画效果
     * 3. 改变布局惨呼
     * 操作稍微复杂,适用于有交互的View
     *
     * 弹性滑动的实现方式:Scroller, 动画, Handler.postDelayed,Thread.sleep
     */
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                customView.smoothScrollTo(count, count);
                break;
            case R.id.btn2:
                customView.scrollTo(count, count);
                break;
            case R.id.btn3:
                customView.scrollBy(count, count);
                break;
            case R.id.btn4:
                ObjectAnimator.ofFloat(customView, "translationY",
                        0, count).setDuration(800).start();
                break;
            case R.id.btn5:
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
                params.width += count;
                params.bottomMargin += count;
                customView.requestLayout();
                //或者
//                customView.setLayoutParams(params);
                break;
            case R.id.btn6:
                relativeBlue.setVisibility(relativeBlue.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;

        }
    }
}
