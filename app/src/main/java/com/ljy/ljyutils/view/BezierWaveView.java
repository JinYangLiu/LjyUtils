package com.ljy.ljyutils.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ljy.util.LjyColorUtil;

/**
 * Created by Mr.LJY on 2017/7/4.
 */

public class BezierWaveView extends View {
    private Paint mPaint;
    private Path mPath;
    private float mWaveLength = 1000;//波浪的长度
    private long mDuration = 1000;//动画执行时长
    private int mOffSet;//平移偏移量
    private int mScreenWidth, mScreenHeight;//屏幕宽高
    private int mWaveCount;
    private int mCenterY;
    private int maxTop = 80;//正选曲线的峰值
    private int paintColor= LjyColorUtil.getInstance().randomColor();

    public BezierWaveView(Context context) {
        super(context);
        new BezierWaveView(context, null);
    }

    public BezierWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofFloat(0f, mWaveLength);
                animator.setDuration(mDuration);
                animator.setRepeatCount(ValueAnimator.INFINITE);//动画无限重复
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mOffSet = (int) animation.getAnimatedValue();
                        postInvalidate();
                        mCenterY-=2;
                        if (mCenterY<0){
                            mCenterY=mScreenHeight;
                        }
                    }
                });
                animator.start();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mScreenWidth = w;
        mScreenHeight = h;
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        mCenterY = mScreenHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //移到屏幕最左边
        mPath.moveTo(-mWaveLength + mOffSet, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            Point a = new Point((int) (-mWaveLength * 3 / 4 + i * mWaveLength + mOffSet), mCenterY + maxTop);
            Point b = new Point((int) (-mWaveLength / 2 + i * mWaveLength + mOffSet), mCenterY);
            Point c = new Point((int) (-mWaveLength / 4 + i * mWaveLength + mOffSet), mCenterY - maxTop);
            Point d = new Point((int) (i * mWaveLength + mOffSet), mCenterY);
            mPath.quadTo(a.x, a.y, b.x, b.y);
            mPath.quadTo(c.x, c.y, d.x, d.y);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(2);
//            mPaint.setColor(Color.YELLOW);
//            canvas.drawPath(mPath, mPaint);
//            mPaint.setStrokeWidth(10);
//            mPaint.setColor(Color.GREEN);
//            canvas.drawPoint(a.x, a.y, mPaint);
//            canvas.drawPoint(b.x, b.y, mPaint);
//            canvas.drawPoint(c.x, c.y, mPaint);
//            canvas.drawPoint(d.x, d.y, mPaint);
        }
        mPath.lineTo(mScreenWidth,mScreenHeight);
        mPath.lineTo(0,mScreenHeight);
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(paintColor);
        canvas.drawPath(mPath, mPaint);

    }
}
