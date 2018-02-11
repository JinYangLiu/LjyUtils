package com.ljy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Mr.LJY on 2018/1/13.
 * <p>
 */
public class LjyFishView extends RelativeLayout {

    public static final int STROKE_WIDTH = 8;
    public static final float DEFAULT_RADIUS = 150;

    private int mScreenWidth;
    private int mScreenHeight;
    private ImageView ivFish;
    private FishDrawable fishDrawable;
    private ObjectAnimator animator;
    private ObjectAnimator rippleAnimator;
    private Paint mPaint;
    private int alpha = 100;
    private Canvas canvas;

    private float x = 0;
    private float y = 0;
    private float radius = 0;


    public LjyFishView(Context context) {
        this(context, null);
    }

    public LjyFishView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LjyFishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStuff(context);
    }

    /**
     * 起点\长度\角度计算终点
     * 正逆负顺
     *
     * @param startPoint
     * @param length
     * @param angle
     * @return
     */
    private static PointF calculatPoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) Math.cos(Math.toRadians(angle)) * length;
        //符合Android坐标的y轴朝下的标准
        float deltaY = (float) Math.sin(Math.toRadians(angle - 180)) * length;
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    /**
     * 线和x轴夹角
     *
     * @param start
     * @param end
     * @return
     */
    public static float calcultatAngle(PointF start, PointF end) {
        return includedAngle(start, new PointF(start.x + 1, start.y), end);
    }

    /**
     * 利用向量的夹角公式计算夹角
     * cosBAC = (AB*AC)/(|AB|*|AC|)
     * 其中AB*AC是向量的数量积AB=(Bx-Ax,By-Ay)  AC=(Cx-Ax,Cy-Ay),AB*AC=(Bx-Ax)*(Cx-Ax)+(By-Ay)*(Cy-Ay)
     *
     * @param center 顶点 A
     * @param head   点1  B
     * @param touch  点2  C
     * @return
     */
    public static float includedAngle(PointF center, PointF head, PointF touch) {
        float abc = (head.x - center.x) * (touch.x - center.x) + (head.y - center.y) * (touch.y - center.y);
        float angleCos = (float) (abc /
                ((Math.sqrt((head.x - center.x) * (head.x - center.x) + (head.y - center.y) * (head.y - center.y)))
                        * (Math.sqrt((touch.x - center.x) * (touch.x - center.x) + (touch.y - center.y) * (touch.y - center.y)))));
        System.out.println(angleCos + "angleCos");

        float temAngle = (float) Math.toDegrees(Math.acos(angleCos));
        //判断方向  正左侧  负右侧 0线上,但是Android的坐标系Y是朝下的，所以左右颠倒一下
        float direction = (center.x - touch.x) * (head.y - touch.y) - (center.y - touch.y) * (head.x - touch.x);
        if (direction == 0) {
            if (abc >= 0) {
                return 0;
            } else
                return 180;
        } else {
            if (direction > 0) {//右侧顺时针为负
                return -temAngle;
            } else {
                return temAngle;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    private void initStuff(Context context) {
        setWillNotDraw(false);
        getScreenParams();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);


        ivFish = new ImageView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(params);
        fishDrawable = new FishDrawable(context);
        ivFish.setImageDrawable(fishDrawable);

        addView(ivFish);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.canvas == null) {
            this.canvas = canvas;
        }
        //方便刷新透明度
        mPaint.setARGB(alpha, 0, 125, 251);

        canvas.drawCircle(x, y, radius, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        rippleAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, 1f).setDuration(1000);
        rippleAnimator.start();
        makeTrail(new PointF(x, y));
        return super.onTouchEvent(event);
    }

    /**
     * 鱼头是第一控点，中点和头与中点和点击点的夹角的一半是第二个控制点角度
     *
     * @param touch
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeTrail(PointF touch) {
        /**
         * 起点和中点是鱼的身体重心处和点击点，三阶贝塞尔的两个控制点分别是鱼头圆心点和夹角中点。
         * 但是平移不是平移的鱼，而是平移的ImageView，而ImageView的setX，setY方法是相对于ImageView左上角的
         * 所以需要把计算好的贝塞尔曲线做一个平移，让贝塞尔曲线的起点和ImageView重合
         * deltaX,Y即是平移的绝对距离
         */
        float deltaX = fishDrawable.getMiddlePoint().x;
        float deltaY = fishDrawable.getMiddlePoint().y;
        Path path = new Path();
        PointF fishMiddle = new PointF(ivFish.getX() + deltaX, ivFish.getY() + deltaY);
        PointF fishHead = new PointF(ivFish.getX() + fishDrawable.getHeadPoint().x, ivFish.getY() + fishDrawable.getHeadPoint().y);
        //把贝塞尔曲线起始点平移到Imageview的左上角
        path.moveTo(fishMiddle.x - deltaX, fishMiddle.y - deltaY);
        final float angle = includedAngle(fishMiddle, fishHead, touch);
        float delta = calcultatAngle(fishMiddle, fishHead);
        PointF controlF = calculatPoint(fishMiddle, 1.6f * fishDrawable.HEAD_RADIUS, angle / 2 + delta);
        //把贝塞尔曲线的所有控制点和结束点都做平移处理
        path.cubicTo(fishHead.x - deltaX, fishHead.y - deltaY, controlF.x - deltaX, controlF.y - deltaY, touch.x - deltaX, touch.y - deltaY);

        final float[] tan = new float[2];
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        animator = ObjectAnimator.ofFloat(ivFish, "x", "y", path);
        animator.setDuration(2 * 1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        //动画启动和结束时设置鱼鳍摆动动画，同时控制鱼身摆动频率
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fishDrawable.setWaveFrequence(1f);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fishDrawable.setWaveFrequence(2f);
                ObjectAnimator finsAnimator = fishDrawable.getFinsAnimator();
                finsAnimator.setRepeatCount(new Random().nextInt(3));
                finsAnimator.setDuration((long) ((new Random().nextInt(1) + 1) * 500));
                finsAnimator.start();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                float angle = (float) (Math.toDegrees(Math.atan2(-tan[1], tan[0])));
                Log.e("**-**-**-**", "onAnimationUpdate: " + angle);
                fishDrawable.setMainAngle(angle);
            }
        });
        animator.start();

    }

    /**
     * ObjectAnimators自动执行
     *
     * @param currentValue
     */
    public void setRadius(float currentValue) {
        alpha = (int) (100 * (1 - currentValue) / 2);
        radius = DEFAULT_RADIUS * currentValue;
        invalidate();

    }

    /**
     * 获取屏幕宽高
     */
    public void getScreenParams() {
        WindowManager WM = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;

    }

    static class FishDrawable extends Drawable {
        private static final float HEAD_RADIUS = 30;
        private static final float TOTAL_LENGTH = 6.79f * HEAD_RADIUS;
        private static final float BODY_LENGHT = HEAD_RADIUS * 3.2f; //第一节身体长度
        private static final String TAG = "Jcs_Fishsss";
        private static final int BODY_ALPHA = 220;
        private static final int OTHER_ALPHA = 160;
        private static final int FINS_ALPHA = 100;
        private static final int FINS_LEFT = 1;//左鱼鳍
        private static final int FINS_RIGHT = -1;
        private static final float FINS_LENGTH = HEAD_RADIUS * 1.3f;
        //	private float mainAngle =90;//角度表示的角
        private ObjectAnimator finsAnimator;
        private Paint mPaint;
        //控制区域
        private int currentValue = 0;//全局控制标志
        private float mainAngle = new SecureRandom().nextFloat() * 360;//角度表示的角
        private float waveFrequence = 1;
        //鱼头点
        private PointF headPoint;
        //转弯更自然的中心点
        private PointF middlePoint;
        private float finsAngle = 0;
        private Paint bodyPaint;
        private Path mPath;

        public FishDrawable(Context context) {
            init();
        }

        public  float getTotalLength() {
            return TOTAL_LENGTH;
        }

        /**
         * 输入起点、长度、旋转角度计算终点
         *
         * @param startPoint 起点
         * @param length     长度
         * @param angle      旋转角度
         * @return 计算结果点
         */
        private PointF calculatPoint(PointF startPoint, float length, float angle) {
            float deltaX = (float) Math.cos(Math.toRadians(angle)) * length;
            //符合Android坐标的y轴朝下的标准
            float deltaY = (float) Math.sin(Math.toRadians(angle - 180)) * length;
            return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
        }

        private void init() {

            //路径
            mPath = new Path();
            //画笔
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setDither(true);//防抖
            mPaint.setColor(Color.argb(OTHER_ALPHA, 244, 92, 71));
            //身体画笔
            bodyPaint = new Paint();
            bodyPaint.setAntiAlias(true);
            bodyPaint.setStyle(Paint.Style.FILL);
            bodyPaint.setDither(true);//防抖
            bodyPaint.setColor(Color.argb(OTHER_ALPHA + 5, 244, 92, 71));
//        middlePoint = new PointF(TOTAL_LENGTH + BODY_LENGHT / 2, TOTAL_LENGTH + BODY_LENGHT / 2);
            middlePoint = new PointF(4.18f * HEAD_RADIUS, 4.18f * HEAD_RADIUS);

            //鱼鳍灵动动画
            finsAnimator = ObjectAnimator.ofFloat(this, "finsAngle", 0f, 1f, 0f);
            finsAnimator.setRepeatMode(ValueAnimator.REVERSE);
            finsAnimator.setRepeatCount(new Random().nextInt(3));

            //引擎部分
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 540 * 100);
            valueAnimator.setDuration(180 * 1000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentValue = (int) (animation.getAnimatedValue());
//				mainAngle = currentValue % 360;
                    invalidateSelf();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    finsAnimator.start();
                }
            });
            valueAnimator.start();

        }

        public PointF getHeadPoint() {
            return headPoint;
        }

        /**
         * 设置头的位置
         *
         * @param headPoint
         */
        public void setHeadPoint(PointF headPoint) {
            this.headPoint = headPoint;
        }

        /**
         * 获取当前角度
         *
         * @return
         */
        public float getMainAngle() {
            return mainAngle;
        }

        /**
         * 设置身体主轴线方向角度
         *
         * @param mainAngle
         */
        public void setMainAngle(float mainAngle) {
            this.mainAngle = mainAngle;
        }

        public ObjectAnimator getFinsAnimator() {
            return finsAnimator;
        }

        public PointF getMiddlePoint() {
            return middlePoint;
        }

        public void setMiddlePoint(PointF middlePoint) {
            this.middlePoint = middlePoint;
        }

        //画身子

        @Override
        public void draw(Canvas canvas) {

            //生成一个半透明图层，否则与背景白色形成干扰,尺寸必须与view的大小一致否则鱼显示不全
            canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), 240, Canvas.ALL_SAVE_FLAG);
            makeBody(canvas, HEAD_RADIUS);
            canvas.restore();
            mPath.reset();
            mPaint.setColor(Color.argb(OTHER_ALPHA, 244, 92, 71));
        }

        /**
         * 主方向是头到尾的方向跟X轴正方向的夹角（顺时针为正）
         * 前进方向和主方向相差180度
         * R + 3.2R
         *
         * @param canvas
         * @param headRadius
         */
        private void makeBody(Canvas canvas, float headRadius) {
            //sin参数为弧度值
            //现有角度=原始角度+ sin（域值[-1，1]）*可摆动的角度   sin作用是控制周期摆动
            float angle = mainAngle + (float) Math.sin(Math.toRadians(currentValue * 1.2 * waveFrequence)) * 2;//中心轴线加偏移量和X轴顺时针方向夹角
            headPoint = calculatPoint(middlePoint, BODY_LENGHT / 2, mainAngle);
            //画头
            canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
            //右鳍 起点
            PointF pointFinsRight = calculatPoint(headPoint, headRadius * 0.9f, angle - 110);
            makeFins(canvas, pointFinsRight, FINS_RIGHT, angle);
            //左鳍 起点
            PointF pointFinsLeft = calculatPoint(headPoint, headRadius * 0.9f, angle + 110);
            makeFins(canvas, pointFinsLeft, FINS_LEFT, angle);

            PointF endPoint = calculatPoint(headPoint, BODY_LENGHT, angle - 180);
            //躯干2
            PointF mainPoint = new PointF(endPoint.x, endPoint.y);
            makeSegments(canvas, mainPoint, headRadius * 0.7f, 0.6f, angle);
            PointF point1, point2, point3, point4, contralLeft, contralRight;
            //point1和4的初始角度决定发髻线的高低值越大越低
            point1 = calculatPoint(headPoint, headRadius, angle - 80);
            point2 = calculatPoint(endPoint, headRadius * 0.7f, angle - 90);
            point3 = calculatPoint(endPoint, headRadius * 0.7f, angle + 90);
            point4 = calculatPoint(headPoint, headRadius, angle + 80);
            //决定胖瘦
            contralLeft = calculatPoint(headPoint, BODY_LENGHT * 0.56f, angle - 130);
            contralRight = calculatPoint(headPoint, BODY_LENGHT * 0.56f, angle + 130);
            mPath.reset();
            mPath.moveTo(point1.x, point1.y);
            mPath.quadTo(contralLeft.x, contralLeft.y, point2.x, point2.y);
            mPath.lineTo(point3.x, point3.y);
            mPath.quadTo(contralRight.x, contralRight.y, point4.x, point4.y);
            mPath.lineTo(point1.x, point1.y);

            mPaint.setColor(Color.argb(BODY_ALPHA, 244, 92, 71));
            //画最大的身子
            canvas.drawPath(mPath, mPaint);
        }

        /**
         * 第二节节肢
         * 0.7R * 0.6 =1.12R
         *
         * @param canvas
         * @param mainPoint
         * @param segmentRadius
         * @param MP            梯形上边下边长度比
         */
        private void makeSegments(Canvas canvas, PointF mainPoint, float segmentRadius, float MP, float fatherAngle) {
            float angle = fatherAngle + (float) Math.cos(Math.toRadians(currentValue * 1.5 * waveFrequence)) * 15;//中心轴线和X轴顺时针方向夹角
            //身长
            float segementLenght = segmentRadius * (MP + 1);
            PointF endPoint = calculatPoint(mainPoint, segementLenght, angle - 180);

            PointF point1, point2, point3, point4;
            point1 = calculatPoint(mainPoint, segmentRadius, angle - 90);
            point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90);
            point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90);
            point4 = calculatPoint(mainPoint, segmentRadius, angle + 90);

            canvas.drawCircle(mainPoint.x, mainPoint.y, segmentRadius, mPaint);
            canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint);
            mPath.reset();
            mPath.moveTo(point1.x, point1.y);
            mPath.lineTo(point2.x, point2.y);
            mPath.lineTo(point3.x, point3.y);
            mPath.lineTo(point4.x, point4.y);
            canvas.drawPath(mPath, mPaint);

            //躯干2
            PointF mainPoint2 = new PointF(endPoint.x, endPoint.y);
            makeSegmentsLong(canvas, mainPoint2, segmentRadius * 0.6f, 0.4f, angle);
        }

        /**
         * 第三节节肢
         * 0.7R * 0.6 * (0.4 + 2.7) + 0.7R * 0.6 * 0.4=1.302R + 0.168R
         *
         * @param canvas
         * @param mainPoint
         * @param segmentRadius
         * @param MP            梯形上边下边长度比
         */
        private void makeSegmentsLong(Canvas canvas, PointF mainPoint, float segmentRadius, float MP, float fatherAngle) {
            float angle = fatherAngle + (float) Math.sin(Math.toRadians(currentValue * 1.5 * waveFrequence)) * 35;//中心轴线和X轴顺时针方向夹角
            //身长
            float segementLenght = segmentRadius * (MP + 2.7f);
            PointF endPoint = calculatPoint(mainPoint, segementLenght, angle - 180);

            PointF point1, point2, point3, point4;
            point1 = calculatPoint(mainPoint, segmentRadius, angle - 90);
            point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90);
            point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90);
            point4 = calculatPoint(mainPoint, segmentRadius, angle + 90);

            makeTail(canvas, mainPoint, segementLenght, segmentRadius, angle);


            canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint);
            mPath.reset();
            mPath.moveTo(point1.x, point1.y);
            mPath.lineTo(point2.x, point2.y);
            mPath.lineTo(point3.x, point3.y);
            mPath.lineTo(point4.x, point4.y);
            canvas.drawPath(mPath, mPaint);
        }

        /**
         * 鱼鳍
         *
         * @param canvas
         * @param startPoint
         * @param type
         */
        private void makeFins(Canvas canvas, PointF startPoint, int type, float fatherAngle) {
            float contralAngle = 115;//鱼鳍三角控制角度
            mPath.reset();
            mPath.moveTo(startPoint.x, startPoint.y);
            PointF endPoint = calculatPoint(startPoint, FINS_LENGTH, type == FINS_RIGHT ? fatherAngle - finsAngle - 180 : fatherAngle + finsAngle + 180);
            PointF contralPoint = calculatPoint(startPoint, FINS_LENGTH * 1.8f, type == FINS_RIGHT ?
                    fatherAngle - contralAngle - finsAngle : fatherAngle + contralAngle + finsAngle);
            mPath.quadTo(contralPoint.x, contralPoint.y, endPoint.x, endPoint.y);
            mPath.lineTo(startPoint.x, startPoint.y);
            mPaint.setColor(Color.argb(FINS_ALPHA, 244, 92, 71));
            canvas.drawPath(mPath, mPaint);
            mPaint.setColor(Color.argb(OTHER_ALPHA, 244, 92, 71));

        }

        /**
         * 鱼尾及鱼尾张合
         *
         * @param canvas
         * @param mainPoint
         * @param length
         * @param maxWidth
         */
        private void makeTail(Canvas canvas, PointF mainPoint, float length, float maxWidth, float angle) {
            float newWidth = (float) Math.abs(Math.sin(Math.toRadians(currentValue * 1.7 * waveFrequence)) * maxWidth + HEAD_RADIUS / 5 * 3);
            //endPoint为三角形底边中点
            PointF endPoint = calculatPoint(mainPoint, length, angle - 180);
            PointF endPoint2 = calculatPoint(mainPoint, length - 10, angle - 180);
            PointF point1, point2, point3, point4;
            point1 = calculatPoint(endPoint, newWidth, angle - 90);
            point2 = calculatPoint(endPoint, newWidth, angle + 90);
            point3 = calculatPoint(endPoint2, newWidth - 20, angle - 90);
            point4 = calculatPoint(endPoint2, newWidth - 20, angle + 90);
            //内
            mPath.reset();
            mPath.moveTo(mainPoint.x, mainPoint.y);
            mPath.lineTo(point3.x, point3.y);
            mPath.lineTo(point4.x, point4.y);
            mPath.lineTo(mainPoint.x, mainPoint.y);
            canvas.drawPath(mPath, mPaint);
            //外
            mPath.reset();
            mPath.moveTo(mainPoint.x, mainPoint.y);
            mPath.lineTo(point1.x, point1.y);
            mPath.lineTo(point2.x, point2.y);
            mPath.lineTo(mainPoint.x, mainPoint.y);
            canvas.drawPath(mPath, mPaint);

        }

        private void setFinsAngle(float currentValue) {
            finsAngle = 45 * currentValue;
        }

        public void setWaveFrequence(float waveFrequence) {
            this.waveFrequence = waveFrequence;
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        /**
         * 高度要容得下两个鱼身长度
         * 8.36计算过程 身长6.79减去头顶到中部位置的长度2.6 再乘以2
         *
         * @return
         */
        @Override
        public int getIntrinsicHeight() {
            return (int) (8.38f * HEAD_RADIUS);
        }

        @Override
        public int getIntrinsicWidth() {
            return (int) (8.38f * HEAD_RADIUS);
        }
    }

}
