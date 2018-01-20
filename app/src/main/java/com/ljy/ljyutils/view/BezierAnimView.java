package com.ljy.ljyutils.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Mr.LJY on 2017/7/4.
 */

public class BezierAnimView extends View {
    private int RADIUS = 20;
    private Point currentPoint;
    private final Paint mPaint;
    private Point controlPoint;

    public BezierAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        controlPoint = new Point(getWidth() / 2, getHeight() / 2);
        if (currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
        } else {
            drawCircle(canvas);
            drawPoint(canvas);
        }

    }

    /**
     * bezier曲线动画
     */
    private void startAnimation() {
        Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, RADIUS);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(controlPoint), startPoint, endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.start();
    }

    private void drawPoint(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(20);
        canvas.drawPoint(controlPoint.x, controlPoint.y, paint);
    }

    /**
     * 画小球
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        float x = currentPoint.x;
        float y = currentPoint.y;
        mPaint.setColor(Color.RED);
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    class BezierEvaluator implements TypeEvaluator<Point> {
        private final Point controlPoint;

        public BezierEvaluator(Point controlPoint) {
            this.controlPoint = controlPoint;
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            int x = (int) ((1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * controlPoint.x + fraction * fraction * endValue.x);
            int y = (int) ((1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * controlPoint.y + fraction * fraction * endValue.y);
            Log.i("ljy", "fraction:" + fraction);
            return new Point(x, y);
        }
    }

}
