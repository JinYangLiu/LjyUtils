package com.ljy.ljyutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ljy.util.LjyLogUtil;

/**
 * Created by LJY on 2018/4/28 16:00
 */
public class MyRelativeLayout extends RelativeLayout {
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LjyLogUtil.i("ParentView.OnTouchListener");
//                OnTouchListener优先级要高于onTouchEvent
                //如果此处返回true,则onTouchEvent不再调用,OnClickListener当然也不会调用了
                //false则onTouchEvent继续处理
                return false;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //优先级最低
                LjyLogUtil.i("ParentView.OnClickListener");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //分发当前事件
        LjyLogUtil.i("ParentView.dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //是否拦截当前事件
        LjyLogUtil.i("ParentView.onInterceptTouchEvent");
        //此处若为true则不再向子View传递此事件
//        return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LjyLogUtil.i("ParentView.onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LjyLogUtil.i("ParentView.ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                LjyLogUtil.i("ParentView.ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

}
