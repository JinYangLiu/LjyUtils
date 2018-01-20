package com.ljy.ljyutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mr.LJY on 2017/7/4.
 */

public class Bezier3 extends View {
    // 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    private static final float C = 0.551915024494f;
    private float[] mData = new float[8];//顺时针绘制圆的4个数据点
    private float[] mCtrl = new float[16];//顺时针控制圆的8个控制点
    private float mCircleRadius = 200;//圆的半径
    private float mDifference = mCircleRadius * C;//圆形的控制点与数据点的差值
    private Paint mPaint;
    private int mCenterX, mCenterY;

    private  float mCurrent=0;//已进行时长
    private float mDuration=1500;//变化总时长
    private float mCount=100;//将时长划分次数
    private  float mPiece=mDuration/mCount;//每一份的时长

    public Bezier3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        //初始化数据点
        mData[0] = 0;
        mData[1] = mCircleRadius;

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = -mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;
        //初始化控制点
        mCtrl[0] = mData[0] + mDifference;
        mCtrl[1] = mData[1];

        mCtrl[2] = mData[2];
        mCtrl[3] = mData[3] + mDifference;

        mCtrl[4] = mData[2];
        mCtrl[5] = mData[3] - mDifference;

        mCtrl[6] = mData[4] + mDifference;
        mCtrl[7] = mData[5];

        mCtrl[8] = mData[4] - mDifference;
        mCtrl[9] = mData[5];

        mCtrl[10] = mData[6];
        mCtrl[11] = mData[7] - mDifference;

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7] + mDifference;

        mCtrl[14] = mData[0] - mDifference;
        mCtrl[15] = mData[1];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制坐标系
        drawCoordinateSystem(canvas);
        //绘制辅助点/线
        drawAuxiliaryLine(canvas);
        //绘制bezier曲线
        drawBezierLine(canvas);

    }

    /**
     * 绘制坐标系
     *
     * @param canvas
     */
    private void drawCoordinateSystem(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);//移到画布中央
        canvas.scale(1,-1);//反转y轴
        Paint fuZhuPaint = new Paint();
        fuZhuPaint.setColor(Color.GRAY);
        fuZhuPaint.setStrokeWidth(3);
        fuZhuPaint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0, -2000, 0, 2000, fuZhuPaint);
        canvas.drawLine(-2000, 0, 2000, 0, fuZhuPaint);

        canvas.restore();
    }

    /**
     * 绘制辅助点/线
     * @param canvas
     */
    private void drawAuxiliaryLine(Canvas canvas) {
        //移到画布中央
        canvas.translate(mCenterX, mCenterY);
        canvas.scale(1,-1);//反转y轴
        mPaint.setStrokeWidth(10);
        //数据点
        mPaint.setColor(Color.BLUE);
        for (int i=0;i<8;i+=2){
            canvas.drawPoint(mData[i],mData[i+1],mPaint);
        }
        //控制点
        mPaint.setColor(Color.GREEN);
        for (int i=0;i<16;i+=2){
            canvas.drawPoint(mCtrl[i],mCtrl[i+1],mPaint);
        }
        //辅助线
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(2);
        for (int i=2,j=2;i<8;i+=2,j+=4){
            canvas.drawLine(mData[i],mData[i+1],mCtrl[j],mCtrl[j+1],mPaint);
            canvas.drawLine(mData[i],mData[i+1],mCtrl[j+2],mCtrl[j+3],mPaint);
        }
        canvas.drawLine(mData[0],mData[1],mCtrl[0],mCtrl[1],mPaint);
        canvas.drawLine(mData[0],mData[1],mCtrl[14],mCtrl[15],mPaint);
    }

    /**
     * 绘制bezier曲线
     * @param canvas
     */
    private void drawBezierLine(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(6);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        Path path=new Path();
        path.moveTo(mData[0],mData[1]);
        path.cubicTo(mCtrl[0],mCtrl[1],mCtrl[2],mCtrl[3],mData[2],mData[3]);
        path.cubicTo(mCtrl[4],mCtrl[5],mCtrl[6],mCtrl[7],mData[4],mData[5]);
        path.cubicTo(mCtrl[8],mCtrl[9],mCtrl[10],mCtrl[11],mData[6],mData[7]);
        path.cubicTo(mCtrl[12],mCtrl[13],mCtrl[14],mCtrl[15],mData[0],mData[1]);
        canvas.drawPath(path,mPaint);

        mCurrent+=mPiece;
        if (mCurrent<mDuration){
            mData[1]-=120/mCount;
            mCtrl[7]+=80/mCount;
            mCtrl[9]+=80/mCount;
            mCtrl[4]-=20/mCount;
            mCtrl[10]+=20/mCount;
            postInvalidateDelayed((long) mPiece);
        }
    }
}
