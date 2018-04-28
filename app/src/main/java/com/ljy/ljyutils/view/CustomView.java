package com.ljy.ljyutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyScreenUtils;

/**
 * Created by Mr.LJY on 2018/3/26.
 */

public class CustomView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private Scroller scroller;
    private GestureDetector gestureDetector;
    //    VelocityTracker:速度追踪
    private VelocityTracker velocityTracker;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float dpi = LjyDensityUtil.getDPI(context);
        LjyLogUtil.i("dpi:" + dpi);
        int screenWidth = LjyScreenUtils.getScreenWidth(context);
        LjyLogUtil.i("screenWidth:" + screenWidth);
        int screenHeight = LjyScreenUtils.getScreenHeight(context);
        LjyLogUtil.i("screenHeight:" + screenHeight);
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        LjyLogUtil.i("系统能识别出的滑动最小距离:touchSlop=" + touchSlop);

        gestureDetector = new GestureDetector(getContext(), this);

        scroller = new Scroller(getContext());
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 1. 获取测量模式（Mode）
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        LjyLogUtil.i("widthSpecMode:" + widthSpecMode);
        // 2. 获取测量大小（Size）
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        LjyLogUtil.i("widthSpecSize :" + widthSpecSize);
        // 3. 通过Mode 和 Size 生成新的SpecMode
        int newWidthSpec = MeasureSpec.makeMeasureSpec(widthSpecSize * 2, widthSpecMode);
//        super.onMeasure(newWidthSpec, heightMeasureSpec);

    }

    /**
     * 布局
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed,left,top,right,bottom);
        LjyLogUtil.i(String.format("changed_%b,top_%d,left_%d,bottom_%d,right_%d", changed, top, left, bottom, right));
        LjyLogUtil.i(String.format("getXX--->top_%d,left_%d,bottom_%d,right_%d", getTop(), getLeft(), getBottom(), getRight()));
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.thin_red));
        canvas.drawCircle(width / 2, height / 2, width / 6, paint);
        LjyLogUtil.i("onDraw");
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 慢慢滑动到指定位置
     *
     * @param destX
     * @param destY
     */
    public void smoothScrollTo(int destX, int destY) {

        int scrollX = getScrollX();
        LjyLogUtil.i("scrollX:" + scrollX);
        int scrollY = getScrollY();
        LjyLogUtil.i("scrollY:" + scrollY);
        int deltaX = destX - scrollX;
        int deltaY = destY - scrollY;

        scroller.startScroll(scrollX, scrollY, deltaX, deltaY,900);

        invalidate();
    }

    private int firstX, firstY, lastX, lastY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //左上角在父控件中的坐标
                LjyLogUtil.i("getX:" + getX());
                LjyLogUtil.i("getY:" + getY());
                //当前点对与左上角的坐标
                LjyLogUtil.i("event.getX:" + event.getX());
                LjyLogUtil.i("event.getY:" + event.getY());
                //当前点的绝对坐标
                LjyLogUtil.i("event.rawX:" + event.getRawX());
                LjyLogUtil.i("event.rawY:" + event.getRawY());
                LjyLogUtil.i("onTouchEvent.ACTION_DOWN");

                lastX = firstX = (int) event.getX();
                lastY = firstY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LjyLogUtil.i("onTouchEvent.ACTION_MOVE");
                scrollTo(firstX-lastX , firstY-lastY );
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                LjyLogUtil.i("onTouchEvent.ACTION_UP");
                velocityTracker.computeCurrentVelocity(1000);//计算1000ms内的速度
                int xVelocity = (int) velocityTracker.getXVelocity();
                LjyLogUtil.i("xVelocity:" + xVelocity);
                int yVelocity = (int) velocityTracker.getYVelocity();
                LjyLogUtil.i("yVelocity:" + yVelocity);
                //用完要记得重置回收
                velocityTracker.clear();
                velocityTracker.recycle();
                velocityTracker = null;
                break;
        }

        //手势监听
        return gestureDetector.onTouchEvent(event);
//        return true;//true,表示不消费,继续传递
//        return super.onTouchEvent(event);
    }

    //OnGestureListener start

    @Override
    public boolean onDown(MotionEvent e) {
        //手指轻触屏幕的一瞬,由ACTION_DOWN触发
        LjyLogUtil.i("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        手指轻触屏幕尚未松开或拖动,由ACTION_DOWN触发
        LjyLogUtil.i("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //轻触屏幕后,手指松开,单击行为,由ACTION_UP触发
        LjyLogUtil.i("onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //手指按下并拖动,由ACTION_DOWN+多个ACTION_MOVE触发
        LjyLogUtil.i("onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长按屏幕
        LjyLogUtil.i("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //快速滑动后松开
        LjyLogUtil.i("onFling");
        return true;
    }

    //OnGestureListener end

    //OnDoubleTapListener start

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //严格的单击行为,不可能是双击中的一次
        LjyLogUtil.i("onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //双击
        LjyLogUtil.i("onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //发生了双击行为(双击期间ACTION_DOWN,ACTION_MOVE,ACTION_UP都会触发此回调)
        LjyLogUtil.i("onDoubleTapEvent");
        return true;
    }

    //OnDoubleTapListener end

}
