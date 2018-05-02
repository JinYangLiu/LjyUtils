package com.ljy.ljyutils.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.view.CustomView;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 1.View的滑动
 * 2.View的事件分发机制
 * 3.View的工作原理和自定义View实现方式
 *      a.ViewRoot对应于ViewRootImpl类,是连接WindowManager和DecorView的纽带,
 *      b.View的绘制流程是从ViewRoot的performTraVersals方法开始的经过measure(测量),layout(定位/布局),draw(绘制)三个过程将View绘制出来
 *
 * MeasureSpec的SpecMode有三类:
 * 1.UNSPECIFIED:
 * 父容器不对View有任何限制,要多大就给多大
 * 2.EXACTLY:
 * 父容器已经检测出View所需的精确大小,此时View的最终大小就是SpecSize所指定的值,
 * 对应于LayoutParams中的match_parent和具体的数值这两种模式
 * 3.AT_MOST:
 * 父容器指定了一个可用的大小即SpecSize,View的大小不能大于这个值,
 * 对应于LayoutParams的wrap_content
 *
 * 1.对于DecorView,其MeasureSpec由窗口的尺寸和自身的LayoutParams来共同确定
 * 2.对于普通View,其MeasureSpec由父控件的MeasureSpec和自身的LayoutParams来共同定义,
 * 此外还和View的margin及padding有关,可以参看ViewGroup的measureChildWithMargins和getChildMeasureSpec方法
 *
 * 工作流程之measure:
 *  1.原始的View通过measure方法就完成了其测量过程,通过measure方法完成(final的方法,不能重写,其中会调用onMeasure方法)
 *  2.ViewGroup除了完成自己的测量过程外还会遍历去调用所有子元素的measure方法,各个子元素再递归去执行这个流程
 *  3.measure完成后,通过getMeasuredWidth/Height可以正确的获取到View的测量宽高,但在某些极端情况下系统可能
 *  需要多次measure才能确定最终的测量宽高,这种情况下,上述方法是不准确的
 *  (一个比较好的习惯是在onLayout方法中去获取View的宽高)
 *  4.在Activity中获取View的宽高: 由于View的measure过程和Activity的生命周期方法不是同步执行的,无法保证View是否测量完毕;
 *      a. Activity/View的onWindowFocusChanged方法(含义是View已经初始化完毕)
 *          activity中此方法会被调用多次(得到焦点和失去焦点都会调用即onResume和onPause时)
 *      b.view.post(runnable),通过post将runnable投递到消息队列的尾部,等待Looper调用此runnable时View已经初始化好了
 *      c.使用ViewTreeObserver的众多回调可以实现,如addOnGlobalLayoutListener
 *      d.view.measure,通过手动对View进行measure来得到,但要分情况处理
 *          1. match_parent: 直接放弃,无法measure出具体的宽高因为需要父控件的剩余空间
 *          2.具体数值:如100px
 *          int width=MeasureSpec.makeMeasureSpec(100,MeasureSpec.EXACTLY);
 *          int height=MeasureSpec.makeMeasureSpec(100,MeasureSpec.EXACTLY);
 *          view.measure(width,height);
 *          int resultW=view.getMeasuredWidth()
 *          3.wrap_content
 *          int width=MeasureSpec.makeMeasureSpec((1<<30)-1,MeasureSpec.AT_MOST);
 *          int height=MeasureSpec.makeMeasureSpec((1<<30)-1,MeasureSpec.AT_MOST);
 *          view.measure(width,height);
 *          int resultW=view.getMeasuredWidth()
 * 工作流程之layout:
 *  作用: ViewGroup用来确定子元素的位置,当ViewGroup的位置确定后,会在onLayout中遍历所有子元素并调用其layout方法,
 *  在layout方法中onLayout又会被调用(layout方法确定view自身位置,onLayout确定子元素位置)
 *  工作流程之draw:
 *  步骤: background.draw(canvas)绘制背景-->onDraw绘制自己-->dispatchDraw绘制子控件-->onDrawScrollBars绘制装饰
 *  dispatchDraw会遍历调用所有子元素的draw方法
 *
 *  自定义View
 *   1.分类:
 *      a.继承View重写onDraw方法
 *      b.继承ViewGroup派生特殊的layout
 *      c.继承特定的View,如Button
 *      d.继承特定的ViewGroup,如LinearLayout
 *  2.注意点
 *      a.支持wrap_content: 具体实现CustomView.onMeasure(),如果不做处理,那么就相当于match_parent
 *      b.支持padding:具体实现CustomView.onDraw()
 *          1. 直接继承view,如果不在draw方法中处理padding,则padding是无法生效的
 *          2. 直接继承ViewGroup,需要在onMeasure和onLayout中考虑padding和子元素的margin做处理
 *      c.不要在View中使用Handler,因为没有必要,view内部已经提供了post方法
 *      d.View中如果有线程或动画,要及时停止,否则可能造成内存泄漏,可在view的onDetachedFromWindow中停止
 *      e.view带有滑动嵌套时,要处理好滑动冲突
 *
 *
 *
 *
 */
public class CustomViewActivity extends BaseActivity {

    @BindView(R.id.customView)
    CustomView customView;
    @BindView(R.id.view)
    Button mView;
    @BindView(R.id.rootScroll)
    LinearLayout rootScroll;
    @BindView(R.id.relativeBlue)
    RelativeLayout relativeBlue;

    //001
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        LjyViewUtil.touchMove(mView);

        int width=customView.getMeasuredWidth();
        int height=customView.getMeasuredHeight();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //002
        customView.post(new Runnable() {
            @Override
            public void run() {
                int width=customView.getMeasuredWidth();
                int height=customView.getMeasuredHeight();
            }
        });
        //003
        ViewTreeObserver observer=customView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                customView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width=customView.getMeasuredWidth();
                int height=customView.getMeasuredHeight();
            }
        });
    }

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

        //不允许ScrollView截断点击事件，点击事件由子View处理
        rootScroll.requestDisallowInterceptTouchEvent(true);
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
     * <p>
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
