package com.ljy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ljy.util.LjyBitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/10/17.
 */

public class LjyDoodleView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder = null;

    // 当前所选画笔的形状
    private BaseAction curAction = null;
    // 默认画笔为黑色
    private int currentColor = Color.BLACK;
    // 画笔的粗细
    private int currentSize = 5;

    private Paint mPaint;

    private List<BaseAction> mBaseActions;

    private Bitmap mBitmap;

    private ActionType mActionType = ActionType.Path;

    public LjyDoodleView(Context context) {
        super(context);
        init();
    }

    public LjyDoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LjyDoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(currentSize);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        mBaseActions = new ArrayList<>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getX();
        float touchY = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setCurAction(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (BaseAction baseAction : mBaseActions) {
                    baseAction.draw(canvas);
                }
                curAction.move(touchX, touchY);
                curAction.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                break;
            case MotionEvent.ACTION_UP:
                mBaseActions.add(curAction);
                curAction = null;
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 得到当前画笔的类型，并进行实例化
     *
     * @param x
     * @param y
     */
    private void setCurAction(float x, float y) {
        switch (mActionType) {
            case Point:
                curAction = new MyPoint(x, y, currentColor);
                break;
            case Path:
                curAction = new MyPath(x, y, currentSize, currentColor);
                break;
            case Line:
                curAction = new MyLine(x, y, currentSize, currentColor);
                break;
            case Rect:
                curAction = new MyRect(x, y, currentSize, currentColor);
                break;
            case Circle:
                curAction = new MyCircle(x, y, currentSize, currentColor);
                break;
            case FillEcRect:
                curAction = new MyFillRect(x, y, currentSize, currentColor);
                break;
            case FilledCircle:
                curAction = new MyFillCircle(x, y, currentSize, currentColor);
                break;
            default:
                break;
        }
    }

    /**
     * 设置画笔的颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        this.currentColor = color;
    }

    /**
     * 设置画笔的粗细
     *
     * @param size 画笔的粗细
     */
    public void setSize(int size) {
        this.currentSize = size;
    }

    /**
     * 设置画笔的形状
     *
     * @param type 画笔的形状
     */
    public void setType(ActionType type) {
        this.mActionType = type;
    }

    /**
     * 将当前的画布转换成一个 Bitmap
     *
     * @return Bitmap
     */
    public Bitmap getBitmap() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        doDraw(canvas);
        return mBitmap;
    }

    /**
     * 保存涂鸦后的图片
     *
     * @param doodleView
     * @return 图片的保存路径
     */
    public String saveBitmap(LjyDoodleView doodleView) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/doodleView/" + System.currentTimeMillis() + ".png";
        if (!new File(path).exists()) {
            new File(path).getParentFile().mkdir();
        }
        LjyBitmapUtil.bitmapToFile(doodleView.getBitmap(),path);
        return path;
    }

    /**
     * 开始进行绘画
     *
     * @param canvas
     */
    private void doDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        for (BaseAction action : mBaseActions) {
            action.draw(canvas);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }


    /**
     * 回退
     *
     * @return 是否已经回退成功
     */
    public boolean back(){
        if(mBaseActions != null && mBaseActions.size() > 0){
            mBaseActions.remove(mBaseActions.size() -1);
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (BaseAction action : mBaseActions) {
                action.draw(canvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
        return false;
    }

    /**
     * 重置签名
     */
    public void reset(){
        if(mBaseActions != null && mBaseActions.size() > 0){
            mBaseActions.clear();
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (BaseAction action : mBaseActions) {
                action.draw(canvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public enum ActionType {
        Point, Path, Line, Rect, Circle, FillEcRect, FilledCircle
    }

    /**
     * 动作的基础类
     * <p>
     * Created by developerHaoz on 2017/7/12.
     */

    abstract class BaseAction {

        public int color;

        BaseAction() {
            color = Color.WHITE;
        }

        BaseAction(int color) {
            this.color = color;
        }

        public abstract void draw(Canvas canvas);

        public abstract void move(float mx, float my);

    }

    class MyPoint extends BaseAction {
        private float x;
        private float y;

        MyPoint(float px, float py, int color) {
            super(color);
            this.x = px;
            this.y = py;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawPoint(x, y, paint);
        }

        @Override
        public void move(float mx, float my) {

        }
    }

    /**
     * 自由曲线
     */
    class MyPath extends BaseAction {
        private Path path;
        private int size;

        MyPath() {
            path = new Path();
            size = 1;
        }

        MyPath(float x, float y, int size, int color) {
            super(color);
            this.path = new Path();
            this.size = size;
            path.moveTo(x, y);
            path.lineTo(x, y);
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPath(path, paint);
        }

        @Override
        public void move(float mx, float my) {
            path.lineTo(mx, my);
        }
    }

    /**
     * 直线
     */
    class MyLine extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyLine() {
            startX = 0;
            startY = 0;
            stopX = 0;
            stopY = 0;
        }

        MyLine(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            stopX = x;
            stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            this.stopX = mx;
            this.stopY = my;
        }
    }

    /**
     * 方框
     */
    class MyRect extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyRect() {
            this.startX = 0;
            this.startY = 0;
            this.stopX = 0;
            this.stopY = 0;
        }

        MyRect(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
        }
    }

    /**
     * 圆框
     */
    class MyCircle extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private float radius;
        private int size;

        MyCircle() {
            startX = 0;
            startY = 0;
            stopX = 0;
            stopY = 0;
            radius = 0;
        }

        MyCircle(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.radius = 0;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
            radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                    + (my - startY) * (my - startY))) / 2);
        }
    }

    class MyFillRect extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyFillRect() {
            this.startX = 0;
            this.startY = 0;
            this.stopX = 0;
            this.stopY = 0;
        }

        MyFillRect(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
        }
    }

    /**
     * 圆饼
     */
    class MyFillCircle extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private float radius;
        private int size;


        public MyFillCircle(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.radius = 0;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawCircle((startX + stopX) / 2, (startX + stopY) / 2, radius, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
            radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                    + (my - startY) * (my - startY))) / 2);
        }
    }
}


















