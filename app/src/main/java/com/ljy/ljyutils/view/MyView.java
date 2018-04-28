package com.ljy.ljyutils.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ljy.util.LjyLogUtil;

/**
 * Created by LJY on 2018/4/28 15:41
 */
public class MyView extends Button {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LjyLogUtil.i("MyView.OnTouchListener");
                return false;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //优先级最低
                LjyLogUtil.i("MyView.OnClickListener");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LjyLogUtil.i("MyView.onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果false不消耗ACTION_DOWN,那么之后的事件将交回其父控件处理
                LjyLogUtil.i("MyView.ACTION_DOWN");
//                return false;
                //若true则会继续触发之后的Move,Up
                //但true之后OnClickListener会失效
                return true;
            case MotionEvent.ACTION_UP:
                LjyLogUtil.i("MyView.ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LjyLogUtil.i("MyView.dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }
}
