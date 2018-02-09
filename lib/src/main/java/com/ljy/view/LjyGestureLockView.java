package com.ljy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ljy.lib.R;
import com.ljy.util.LjySPUtil;
import com.ljy.util.LjyViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2017/12/28.
 * <p>
 * 九宫格图案锁
 */

public class LjyGestureLockView extends View {

    private static final int POINT_SIZE = 1;   //选中点的数量

    private int xNum = 3;
    private int yNum = 3;

    private Point[][] points = new Point[xNum][yNum];  //声明一个3行3列的点

    private boolean isInit;   //是否初始化过

    private float width, height; //布局的宽和高

    private float offsetsX, offsetsY;  //偏移量

    private float bitmapR;  //图片资源的半径

    private float movingX, movingY;  //移动的坐标

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //画笔

    private Bitmap pointNormal, pointPressed, pointError;

    private List<Point> pointList = new ArrayList<Point>(); //存放点的集合

    private boolean isSelect;  //是否选择

    private boolean isFinish;  //是否结束

    private boolean movingNoPoint;  //鼠标在移动但不是九宫格的点  --- 绘制还得继续


    private OnPatterChangeListener listener;   //监听器

    private boolean isLogin;       //判断是否为登录，来切换不同的布局样式

    private int iconPointUncheck;
    private int iconPointCheck;
    private int iconPointError;
    private int iconLinePressed;
    private int iconLineError;

    public boolean getIsLogin() {
        return isLogin;
    }

    private String LOCK_TIME_START = "LOCK_TIME_START";
    private int lockTimeLen = 3;//锁定时长

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public LjyGestureLockView(Context context) {
        this(context, null, 0);
    }

    public LjyGestureLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LjyGestureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LjyGestureLockView);
        //获取对应的属性值
        this.iconPointCheck = typedArray.getResourceId(R.styleable.LjyGestureLockView_iconPointCheck, R.drawable.gesture_check);
        this.iconPointUncheck = typedArray.getResourceId(R.styleable.LjyGestureLockView_iconPointUncheck, R.drawable.gesture_uncheck);
        this.iconPointError = typedArray.getResourceId(R.styleable.LjyGestureLockView_iconPointError, R.drawable.gesture_error);
        this.iconLinePressed = typedArray.getResourceId(R.styleable.LjyGestureLockView_iconLinePressed, R.drawable.line_pressed);
        this.iconLineError = typedArray.getResourceId(R.styleable.LjyGestureLockView_iconLineError, R.drawable.line_error);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!isInit) {
            initPoints();
        }
        //画点
        point2Canvas(canvas);
        //画线
        if (pointList.size() > 0) {
            Point a = pointList.get(0);
            //绘制9宫格里坐标点
            for (int i = 0; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                line2Canvas(canvas, a, b, 0x55432b2b);
//                line2Canvas(canvas,a,b);
                a = b;
            }
            //绘制鼠标坐标点
            if (movingNoPoint) {
//                line2Canvas(canvas,a, new Point(movingX,movingY));
                line2Canvas(canvas, a, new Point(movingX, movingY), 0x55432b2b);
            }
        }
    }

    /**
     * 将点绘制在画布上
     */
    private void point2Canvas(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (point.state == Point.STATE_PRESSED) {
                    canvas.drawBitmap(pointPressed, point.x - bitmapR, point.y - bitmapR, paint);
                } else if (point.state == Point.STATE_ERROR) {
                    canvas.drawBitmap(pointError, point.x - bitmapR, point.y - bitmapR, paint);
                } else {
                    canvas.drawBitmap(pointNormal, point.x - bitmapR, point.y - bitmapR, paint);
                }
            }
        }
    }

    private void line2Canvas(Canvas canvas, Point a, Point b, int color) {
        //线的长度
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
        mPaint.setColor(color);
        mPaint.setStrokeWidth(pointNormal.getHeight() / 5.2f);
        mPaint.setAlpha(80);
        Path mPath = new Path();
        mPath.moveTo(a.x, a.y);
        mPath.lineTo(b.x, b.y);
        if (a.state != Point.STATE_PRESSED) {
            mPaint.setColor(0x55432b2b);
        }
        if (!getIsLogin()) {
            mPaint.setColor(0x55432b2b);
        }
        canvas.drawPath(mPath, mPaint);
    }


    /**
     * 初始化点
     */
    private void initPoints() {
        //1.获取布局的宽和高
        width = getWidth();
        height = getHeight();
        // 2. 横屏和竖屏的偏移量
        // 横屏
        if (width > height) {
            offsetsX = (width - height) / 2;
            width = height;
        }
        //竖屏
        else {
            offsetsY = (height - width) / 2;
            height = width;
        }
        //3.初始化图片资源
        pointNormal = BitmapFactory.decodeResource(getResources(), iconPointUncheck);
        pointPressed = BitmapFactory.decodeResource(getResources(), iconPointCheck);
        pointError = BitmapFactory.decodeResource(getResources(), iconPointError);

        pointNormal = resetBitmapSize(pointNormal);
        pointPressed = resetBitmapSize(pointPressed);
        pointError = resetBitmapSize(pointError);

        //4.点的坐标
        points[0][0] = new Point(offsetsX + width * 18 / 100, offsetsY + width * 18 / 100);
        points[0][1] = new Point(offsetsX + width / 2, offsetsY + width * 18 / 100);
        points[0][2] = new Point(offsetsX + width - width * 18 / 100, offsetsY + width * 18 / 100);

        points[1][0] = new Point(offsetsX + width * 18 / 100, offsetsY + width / 2);
        points[1][1] = new Point(offsetsX + width / 2, offsetsY + width / 2);
        points[1][2] = new Point(offsetsX + width - width * 18 / 100, offsetsY + width / 2);

        points[2][0] = new Point(offsetsX + width * 18 / 100, offsetsY + width - width * 18 / 100);
        points[2][1] = new Point(offsetsX + width / 2, offsetsY + width - width * 18 / 100);
        points[2][2] = new Point(offsetsX + width - width * 18 / 100, offsetsY + width - width * 18 / 100);


        // 5.图片资源的半径
        bitmapR = pointNormal.getHeight() / 2;

        //6.设置密码
        int index = 1;
        for (Point[] points : this.points) {
            for (Point point : points) {
                point.index = index;
                index++;
            }
        }

        //7.初始化完成
        isInit = true;
    }

    private Bitmap resetBitmapSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        float newWidth = LjyViewUtil.getScreenWidth(getContext()) / (xNum + 2f);
        // 计算缩放比例
        float scaleWidth = newWidth / width;
        float scaleHeight = newWidth / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long currentTime = System.currentTimeMillis();
        long needTime = 1 * 60 * 1000 - (currentTime - new LjySPUtil(getContext()).get(LOCK_TIME_START, 0l));
        if (needTime > 0 && (needTime / 1000 <= lockTimeLen * 60)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //重新绘制
                    if (listener != null) {
                        listener.onPatterStart(true);
                    }
                    break;
                default:
                    break;
            }
            return false;
        } else {
            movingX = event.getX();
            movingY = event.getY();
            isFinish = false;
            Point point = null;
            movingNoPoint = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //重新绘制
                    if (listener != null) {
                        listener.onPatterStart(true);
                    }
                    resetPoint();
                    point = checkSelectPoint();
                    if (point != null) {
                        isSelect = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isSelect) {
                        point = checkSelectPoint();
                        if (point == null) {
                            movingNoPoint = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isFinish = true;
                    isSelect = false;
                    break;
                default:
                    break;
            }
            //  如果绘制没有结束并且还在继续画且选中的点还在继续画的时候 ---- 选中重复检查
            if (!isFinish && isSelect && point != null) {
                //交叉点
                if (crossPoint(point)) {
                    movingNoPoint = true;
                }
                //新点
                else {
                    point.state = Point.STATE_PRESSED;
                    pointList.add(point);
                }
            }
            //绘制结束
            if (isFinish) {
                //绘制不成立
                if (pointList.size() == 0) {
                    resetPoint();
                }
                //绘制错误
                else if (pointList.size() < POINT_SIZE && pointList.size() > 0) {
                    errorPoint();
                    if (listener != null) {
                        listener.onPatterChange(null);
                    }
                }
                // 绘制成功
                else {
                    if (listener != null) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("#");
                        for (int i = 0; i < pointList.size(); i++) {
                            stringBuffer.append(pointList.get(i).index);
                        }
                        if (!TextUtils.isEmpty(stringBuffer.toString())) {
                            listener.onPatterChange(stringBuffer.toString());
                        }
                    }
                }
            }
            //刷新View
            postInvalidate();
            return true;
        }
    }

    public void errorState() {
        errorPoint();
        postInvalidate();
    }

    /**
     * 交叉点
     *
     * @param point 点
     * @return 是否交叉
     */
    private boolean crossPoint(Point point) {
        //点的集合是否包含这个点
        if (pointList.contains(point)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置绘制不成立
     */
    public void resetPoint() {
        for (int i = 0; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            point.state = Point.STATE_NORMAL;
        }
        pointList.clear();
        postInvalidate();
    }

    /**
     * 设置绘制错误
     */
    private void errorPoint() {
        for (Point point : pointList) {
            point.state = Point.STATE_ERROR;
        }
    }

    /**
     * 检查是否选中
     */
    private Point checkSelectPoint() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];

                if (point != null && Point.with(point.x, point.y, bitmapR, movingX, movingY)) {
                    return point;
                }
            }
        }
        return null;
    }


    /**
     * 自定义的点
     */
    public static class Point {
        //正常
        static final int STATE_NORMAL = 0;
        //选中
        static final int STATE_PRESSED = 1;
        //错误
        static final int STATE_ERROR = 2;

        public float x, y;
        public int index = 0, state = 0;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * 2点之间的距离
         *
         * @param a 点a
         * @param b 点b
         * @return 距离
         */
        public static double distance(Point a, Point b) {
            //x轴差的平方加上y轴差的平方，对和开方
            return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
        }

        /**
         * 是否重合(是否选中)
         *
         * @param pointX  参考点的X
         * @param pointY  参考点的Y
         * @param r       圆的半径
         * @param movingX 移动点的x
         * @param movingY 移动点的Y
         * @return 是否重合
         */
        public static boolean with(float pointX, float pointY, float r, float movingX, float movingY) {
            //开方与半径判断
            return Math.sqrt((pointX - movingX) * (pointX - movingX) + (pointY - movingY) * (pointY - movingY)) < r;
        }

        // 获取角度
        public static float getDegrees(Point pointA, Point pointB) {
            return (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x));

        }
    }

    /**
     * 图案监听器
     */
    public static interface OnPatterChangeListener {
        /**
         * 图案改变
         *
         * @param passwordStr 图案密码
         */
        void onPatterChange(String passwordStr);

        /**
         * 图案是否重新绘制
         *
         * @param isStart
         */
        void onPatterStart(boolean isStart);
    }

    /**
     * 设置图案监听器
     *
     * @param listener
     */
    public void setOnPatterChangeListener(OnPatterChangeListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }
}

