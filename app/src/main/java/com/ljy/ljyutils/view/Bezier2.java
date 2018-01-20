package com.ljy.ljyutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mr.LJY on 2017/7/4.
 */

public class Bezier2 extends View {
    private PointF start,end,control1,control2;
    private Paint mPaint;
    private int centerX, centerY;
    private boolean tag=true;

    public Bezier2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        start=new PointF(0,0);
        end=new PointF(0,0);
        control1=new PointF(0,0);
        control2=new PointF(0,0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX=w/2;
        centerY =h/2;
        start.x=centerX-200;
        start.y=centerY;
        end.x=centerX+200;
        end.y=centerY;
        control1.x=centerX-100;
        control1.y=centerY-100;
        control2.x=centerX+100;
        control2.y=centerY+100;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tag=event.getX()<getWidth()/2;
        if (tag){
            control1.x=event.getX();
            control1.y=event.getY();
        }else {
            control2.x=event.getX();
            control2.y=event.getY();
        }
        invalidate();
        return  true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制数据点和控制点
        mPaint.setStrokeWidth(20);
        mPaint.setColor(Color.GREEN);
        canvas.drawPoint(start.x,start.y,mPaint);
        canvas.drawPoint(end.x,end.y,mPaint);
        canvas.drawPoint(control1.x,control1.y,mPaint);
        canvas.drawPoint(control2.x,control2.y,mPaint);
        //绘制辅助线
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(start.x,start.y,control1.x,control1.y,mPaint);
        canvas.drawLine(control1.x,control1.y,control2.x,control2.y,mPaint);
        canvas.drawLine(control2.x,control2.y,end.x,end.y,mPaint);
        //绘制bezier曲线
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.RED);
        Path path=new Path();
        path.moveTo(start.x,start.y);
        path.cubicTo(control1.x,control1.y,control2.x,control2.y,end.x,end.y);
        canvas.drawPath(path,mPaint);
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
