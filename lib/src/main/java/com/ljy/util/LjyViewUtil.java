package com.ljy.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Mr.LJY on 2017/12/26.
 * <p>
 * 提供view相关方法
 */

public class LjyViewUtil {

    /**
     * 重新计算ListView item的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * <p>
     * （ScrollView嵌套ListView只显示一行，计算的高度不正确的解决办法）
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * View的宽高=屏幕的宽高*scaleW/H
     *
     * @param context      上下文对象，用于获取屏幕宽高
     * @param layoutParams 目标view的layoutParams
     * @param scaleW       宽的缩放
     * @param scaleH       高的缩放
     * @param type         几种状态:0为正常，1为使高等于宽，2为使宽等于高,3为height=WRAP_CONTENT，4为width=WRAP_CONTENT
     */
    public static void setViewSize(Context context, ViewGroup.LayoutParams layoutParams, float scaleW, float scaleH, int type) {
        if (scaleW <= 0 && scaleH <= 0)
            return;
        if (context == null || layoutParams == null)
            return;
        context = context.getApplicationContext();

        int windowWidth = LjyScreenUtils.getScreenWidth(context);
        int windowHeight = LjyScreenUtils.getScreenHeight(context);
        int viewWidth;
        int viewHeight;
        type = scaleW <= 0 ? 2 : scaleH <= 0 ? 1 : type;
        switch (type) {
            case 0:
                viewWidth = (int) (windowWidth * scaleW);
                viewHeight = (int) (windowHeight * scaleH);
                break;
            case 1:
                viewWidth = (int) (windowWidth * scaleW);
                viewHeight = viewWidth;
                break;
            case 2:
                viewHeight = (int) (windowHeight * scaleH);
                viewWidth = viewHeight;
                break;
            case 3:
                viewWidth = (int) (windowWidth * scaleW);
                viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            case 4:
                viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                viewHeight = (int) (windowHeight * scaleH);
                break;
            default:
                viewWidth = RelativeLayout.LayoutParams.WRAP_CONTENT;
                viewHeight = RelativeLayout.LayoutParams.WRAP_CONTENT;
                break;
        }
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
    }

    public static int getViewWidth(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth();
    }

    public static int getViewHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }
    //1. 该方法测量的宽度和高度可能与视图绘制完成后的真实的宽度和高度不一致。
//        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(width, height);
//        return view.getMeasuredHeight();
    //2.在视图将要绘制时调用该监听事件，会被调用多次，因此获取到视图的宽度和高度后要移除该监听事件。
//        view.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//
//                    @Override
//                    public boolean onPreDraw() {
//                        view.getViewTreeObserver().removeOnPreDrawListener(this);
//                        view.getWidth(); // 获取宽度
//                        view.getHeight(); // 获取高度
//                        return true;
//                    }
//                });
    //3.在布局发生改变或者某个视图的可视状态发生改变时调用该事件，会被多次调用，因此需要在获取到视图的宽度和高度后执行 remove 方法移除该监听事件。
//        view.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        if (Build.VERSION.SDK_INT >= 16) {
//                            view.getViewTreeObserver()
//                                    .removeOnGlobalLayoutListener(this);
//                        }
//                        else {
//                            view.getViewTreeObserver()
//                                    .removeGlobalOnLayoutListener(this);
//                        }
//                        view.getWidth(); // 获取宽度
//                        view.getHeight(); // 获取高度
//                    }
//                });
    //四、重写 View 的 onSizeChanged 方法,在视图的大小发生改变时调用该方法，会被多次调用，因此获取到宽度和高度后需要考虑禁用掉代码。
    //该实现方法需要继承 View，且多次被调用，不建议使用
//        @Override
//        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//            super.onSizeChanged(w, h, oldw, oldh);
//
//            view.getWidth(); // 获取宽度
//            view.getHeight(); // 获取高度
//        }
    //五、重写 View 的 onLayout 方法,该方法会被多次调用，获取到宽度和高度后需要考虑禁用掉代码。
    //该实现方法需要继承 View，且多次被调用，不建议使用。
//        @Override
//        protected void onLayout(boolean changed, int l, int t, int r, int b) {
//            super.onLayout(changed, l, t, r, b);
//            view.getWidth(); // 获取宽度
//            view.getHeight(); // 获取高度
//        }
    //六、使用 View.OnLayoutChangeListener 监听事件（API >= 11）,在视图的 layout 改变时调用该事件，会被多次调用，因此需要在获取到视图的宽度和高度后执行 remove 方法移除该监听事件。
//        view.addOnLayoutChangeListener(
//                new View.OnLayoutChangeListener() {
//
//                    @Override
//                    public void onLayoutChange(View v, int l, int t, int r, int b,
//                                               int oldL, int oldT, int oldR, int oldB) {
//                        view.removeOnLayoutChangeListener(this);
//                        view.getWidth(); // 获取宽度
//                        view.getHeight(); // 获取高度
//                    }
//                });
    //7.Runnable 对象中的方法会在 View 的 measure、layout 等事件完成后触发。
    //UI 事件队列会按顺序处理事件，在 setContentView() 被调用后，事件队列中会包含一个要求重新 layout 的 message，所以任何 post 到队列中的 Runnable 对象都会在 Layout 发生变化后执行。
    //该方法只会执行一次，且逻辑简单，建议使用。

    /**
     * 使view可以拖拽移动
     */
    public static void touchMove(final View view) {
        final View parentView = ((View) view.getParent());
        final int parentViewWidth = getViewWidth(parentView);
        final int parentViewHeight = getViewHeight(parentView);
        view.setOnTouchListener(new View.OnTouchListener() {
            private int lastY;
            private int lastX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = x - lastX;
                        int dy = y - lastY;
                        //边界检测
                        if (dx + v.getX() < 0)
                            dx = (int) -v.getX();
                        if (dy + v.getY() < 0)
                            dy = (int) -v.getY();
                        if (dx + v.getX() + v.getWidth() > parentViewWidth) {
                            dx = (int) (parentViewWidth - v.getWidth() - v.getX());
                        }
                        if (dy + v.getY() + v.getHeight() > parentViewHeight) {
                            dy = (int) (parentViewHeight - v.getHeight() - v.getY());
                        }
                        //方式1:
//                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        param.leftMargin = left;
//                        param.topMargin = top;
//                        v.setLayoutParams(param);
                        //方式2:
                        v.setTranslationX(v.getTranslationX() + dx);
                        v.setTranslationY(v.getTranslationY() + dy);
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;

                }
                lastX = x;
                lastY = y;
                return true;
            }
        });
    }


}
