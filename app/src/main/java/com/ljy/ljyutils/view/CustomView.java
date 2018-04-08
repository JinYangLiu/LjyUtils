package com.ljy.ljyutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ljy.util.LjyDensityUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyScreenUtils;

/**
 * Created by Mr.LJY on 2018/3/26.
 */

public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float dpi= LjyDensityUtil.getDPI(context);
        LjyLogUtil.i("dpi:"+dpi);
        int screenWidth= LjyScreenUtils.getScreenWidth(context);
        LjyLogUtil.i("screenWidth:"+screenWidth);
        int screenHeight=LjyScreenUtils.getScreenHeight(context);
        LjyLogUtil.i("screenHeight:"+screenHeight);
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // LjyLogUtil.i(String.format("--->top_%d,left_%d,bottom_%d,right_%d",getTop(),getLeft(),getBottom(),getRight()));
        // 1. 获取测量模式（Mode）
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        LjyLogUtil.i("widthSpecMode:"+widthSpecMode);
        // 2. 获取测量大小（Size）
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        LjyLogUtil.i("widthSpecSize :"+widthSpecSize );
        // 3. 通过Mode 和 Size 生成新的SpecMode
        int newWidthSpec=MeasureSpec.makeMeasureSpec(widthSpecSize*2, widthSpecMode);
//        super.onMeasure(newWidthSpec, heightMeasureSpec);

    }

    /**
     * 布局
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LjyLogUtil.i(String.format("changed_%b,top_%d,left_%d,bottom_%d,right_%d",changed,top,left,bottom,right));
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LjyLogUtil.i("onDraw");
    }
}
