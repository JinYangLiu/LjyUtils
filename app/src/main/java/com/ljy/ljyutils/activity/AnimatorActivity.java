package com.ljy.ljyutils.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnimatorActivity extends BaseActivity {

    @BindView(R.id.iv_frame)
    ImageView ivFrame;

    @BindView(R.id.btn_anim_frame)
    Button btnAnimFrame;

    @BindView(R.id.btn_anim_value_ofInt)
    Button btnAnimValueOfInt;

    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private AnimationDrawable animDrawable;
    private Animation translateAnimation;
    private TranslateAnimation translateAnimation2;
    private Animation scaleAnimation;
    private ScaleAnimation scaleAnimation2;
    private Animation rotateAnimation;
    private RotateAnimation rotateAnimation2;
    private Animation alphaAnimation;
    private AlphaAnimation alphaAnimation2;
    private Animation setAnimation;
    private AnimationSet setAnimation2;
    private ValueAnimator valueAnimOfInt;
    private ValueAnimator valueAnimOfObject;
    private ObjectAnimator objAnimAlpha;
    private ObjectAnimator objAnimRotation;
    private ObjectAnimator objAnimTrans;
    private ObjectAnimator objAnimScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        ButterKnife.bind(mActivity);
        //1. 帧动画(也属于View动画)
        initFrame();

        //2 补间动画/View动画
        //加载xml中的补间动画
        initTweenXml();
        //java代码直接创建
        initTweenJava();

        //3.属性动画
        //建议使用java代码实现
        //1.value
        initValueAnimJava();
        //2.object
        initObjectAnimJava();


    }

    private void initObjectAnimJava() {
        //透明度
        objAnimAlpha = ObjectAnimator.ofFloat(ivFrame, "alpha", 1f, 0f, 1f);
        objAnimAlpha.setDuration(2000);
        //旋转
        objAnimRotation = ObjectAnimator.ofFloat(ivFrame, "rotationX", 0f, 360f);
        objAnimRotation.setDuration(2000);
        objAnimRotation.setStartDelay(3000);
        //平移
        objAnimTrans = ObjectAnimator.ofFloat(ivFrame, "translationY", 0, 200, -200, 0);
        objAnimTrans.setDuration(2000);
        objAnimTrans.setStartDelay(6000);
        Interpolator interpolator = new BounceInterpolator();//最后阶段弹球效果
        objAnimTrans.setInterpolator(interpolator);
        //缩放
        objAnimScale = ObjectAnimator.ofFloat(ivFrame, "scaleX", 1f, 3f, 1f);
        objAnimScale.setDuration(2000);
        objAnimScale.setStartDelay(9000);
        Interpolator interpolator2 = new OvershootInterpolator();//超出再回到最后位置
        objAnimScale.setInterpolator(interpolator2);

    }

    private void initValueAnimJava() {
        //ofInt
        ofIntInit();

        //ofFloat同理
//        ValueAnimator anim = ValueAnimator.ofFloat(0.1f, 3.5f);

        //ofObject
        ofObjectInit();
    }

    private void ofObjectInit() {
        class Point {

            // 设置两个变量用于记录坐标的位置
            private float x;
            private float y;

            // 构造方法用于设置坐标
            public Point(float x, float y) {
                this.x = x;
                this.y = y;
            }

            // get方法用于获取坐标
            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }
        }

        // 实现TypeEvaluator接口
        class PointEvaluator implements TypeEvaluator {

            // 复写evaluate（）
            // 在evaluate（）里写入对象动画过渡的逻辑
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {

                // 将动画初始值startValue 和 动画结束值endValue 强制类型转换成Point对象
                Point startPoint = (Point) startValue;
                Point endPoint = (Point) endValue;

                // 根据fraction来计算当前动画的x和y的值
                float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
                float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

                // 将计算后的坐标封装到一个新的Point对象中并返回
                Point point = new Point(x, y);
                return point;
            }

        }

        Point startPoint = new Point(0, 0);// 初始点为圆心(70,70)
        Point endPoint = new Point(200, 1000);// 结束点为(700,1000)

        // 步骤2:创建动画对象 & 设置初始值 和 结束值
        valueAnimOfObject = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        // 参数说明
        // 参数1：TypeEvaluator 类型参数 - 使用自定义的PointEvaluator(实现了TypeEvaluator接口)
        // 参数2：初始动画的对象点
        // 参数3：结束动画的对象点

        // 步骤3：设置动画参数
        valueAnimOfObject.setDuration(5000);
        // 设置动画时长

// 步骤3：通过 值 的更新监听器，将改变的对象手动赋值给当前对象
// 此处是将 改变后的坐标值对象 赋给 当前的坐标值对象
        // 设置 值的更新监听器
        // 即每当坐标值（Point对象）更新一次,该方法就会被调用一次
        valueAnimOfObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point currentPoint = (Point) animation.getAnimatedValue();
                // 将每次变化后的坐标值（估值器PointEvaluator中evaluate（）返回的Piont对象值）到当前坐标值对象（currentPoint）
                // 从而更新当前坐标值（currentPoint）

                ivFrame.setX(currentPoint.getX());
                ivFrame.setY(currentPoint.getY());
                // 步骤4：每次赋值后就重新绘制，从而实现动画效果
//                invalidate();
                // 调用invalidate()后,就会刷新View,即才能看到重新绘制的界面,即onDraw()会被重新调用一次
                // 所以坐标值每改变一次,就会调用onDraw()一次
            }
        });
    }

    private void ofIntInit() {
        // 步骤1：设置动画属性的初始值 & 结束值
        int ivWidth = ivFrame.getLayoutParams().width;
        valueAnimOfInt = ValueAnimator.ofInt(ivWidth, ivWidth * 7, ivWidth);
        // ofInt（）作用有两个
        // 1. 创建动画实例
        // 2. 将传入的多个Int参数进行平滑过渡:此处传入0和1,表示将值从0平滑过渡到1
        // 如果传入了3个Int参数 a,b,c ,则是先从a平滑过渡到b,再从b平滑过渡到C，以此类推
        // ValueAnimator.ofInt()内置了整型估值器,直接采用默认的.不需要设置，即默认设置了如何从初始值 过渡到 结束值
        // 关于自定义插值器我将在下节进行讲解
        // 下面看看ofInt()的源码分析 ->>关注1

        // 步骤2：设置动画的播放各种属性
        // 设置动画运行的时长
        valueAnimOfInt.setDuration(3000);

        // 设置动画延迟播放时间
//        valueAnimOfInt.setStartDelay(500);

        // 设置动画重复播放次数 = 重放次数+1
        // 动画播放次数 = infinite时,动画无限重复
        valueAnimOfInt.setRepeatCount(0);

        // 设置重复播放动画模式
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放
        valueAnimOfInt.setRepeatMode(ValueAnimator.RESTART);

        // 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
        // 设置 值的更新监听器
        // 即：值每次改变、变化一次,该方法就会被调用一次
        valueAnimOfInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int currentValue = (Integer) animation.getAnimatedValue();
                // 获得改变后的值

                System.out.println(currentValue);
                // 输出改变后的值

                // 每次值变化时，将值手动赋值给对象的属性
                // 即将每次变化后的值 赋 给按钮的宽度，这样就实现了按钮宽度属性的动态变化
                ivFrame.getLayoutParams().width = currentValue;

                // 步骤4：刷新视图，即重新绘制，从而实现动画效果
                ivFrame.requestLayout();

            }
        });

        /**
         * xml实现：
         * // ValueAnimator采用<animator>  标签
         <animator xmlns:android="http://schemas.android.com/apk/res/android"
         android:valueFrom="0"   // 初始值
         android:valueTo="100"  // 结束值
         android:valueType="intType" // 变化值类型 ：floatType & intType

         android:duration="3000" // 动画持续时间（ms），必须设置，动画才有效果
         android:startOffset ="1000" // 动画延迟开始时间（ms）
         android:fillBefore = “true” // 动画播放完后，视图是否会停留在动画开始的状态，默认为true
         android:fillAfter = “false” // 动画播放完后，视图是否会停留在动画结束的状态，优先于fillBefore值，默认为false
         android:fillEnabled= “true” // 是否应用fillBefore值，对fillAfter值无影响，默认为true
         android:repeatMode= “restart” // 选择重复播放动画模式，restart代表正序重放，reverse代表倒序回放，默认为restart|
         android:repeatCount = “0” // 重放次数（所以动画的播放次数=重放次数+1），为infinite时无限重复
         android:interpolator = @[package:]anim/interpolator_resource // 插值器，即影响动画的播放速度,下面会详细讲

         />

         */

    }


    private void initTweenJava() {
        //平移
        translateAnimation2 = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, -0.5f);
        translateAnimation2.setDuration(16000);
//        translateAnimation2.setStartOffset(500);
//        translateAnimation2.setRepeatCount(3);
//        translateAnimation2.setRepeatMode(Animation.REVERSE);//倒序
//        translateAnimation2.setFillBefore(false);
//        translateAnimation2.setFillAfter(true);

        //缩放
        scaleAnimation2 = new ScaleAnimation(1, 0.3f, 1, 0.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setStartOffset(3000);
        scaleAnimation2.setDuration(1000);

        //旋转
        rotateAnimation2 = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation2.setDuration(1000);
        rotateAnimation2.setRepeatCount(Animation.INFINITE);

        alphaAnimation2 = new AlphaAnimation(1, 0.3f);
        alphaAnimation2.setDuration(1000);
        alphaAnimation2.setStartOffset(6000);
        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LjyLogUtil.i("alphaAnim onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LjyLogUtil.i("alphaAnim onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                LjyLogUtil.i("alphaAnim onAnimationRepeat");
            }
        });


        setAnimation2 = new AnimationSet(true);
        //创建对应的插值器
        Interpolator overshootInterpolator = new OvershootInterpolator();//快速完成动画，超出再回到结束样式
        setAnimation2.setInterpolator(overshootInterpolator);

        setAnimation2.addAnimation(translateAnimation2);
//        setAnimation2.addAnimation(scaleAnimation2);
        setAnimation2.addAnimation(rotateAnimation2);
        setAnimation2.addAnimation(alphaAnimation2);
/**
 * 作用	        资源ID	                                对应的Java类
 动画加速进行 	@android:anim/accelerate_interpolator	AccelerateInterpolator
 快速完成动画，超出再回到结束样式	@android:anim/overshoot_interpolator	OvershootInterpolator
 先加速再减速	@android:anim/accelerate_decelerate_interpolator	AccelerateDecelerateInterpolator
 先退后再加速前进	@android:anim/anticipate_interpolator	AnticipateInterpolator
 先退后再加速前进，超出终点后再回终点	@android:anim/anticipate_overshoot_interpolator	AnticipateOvershootInterpolator
 最后阶段弹球效果	@android:anim/bounce_interpolator	BounceInterpolator
 周期运动	@android:anim/cycle_interpolator	CycleInterpolator
 减速	@android:anim/decelerate_interpolator	DecelerateInterpolator
 匀速	@android:anim/linear_interpolator	LinearInterpolator

 */


        //java实现anim_layout
        // 加载子元素的出场动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
        // 设置LayoutAnimation的属性
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        // 为rlContent设置LayoutAnimation的属性
        rlContent.setLayoutAnimation(controller);

    }

    private void initTweenXml() {
        translateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_translate);

        scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_scale);

        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_rotate);

        alphaAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha);

        setAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_set);

        //场景：
        //启动动画
//        Intent intent = new Intent (this,Acvtivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.enter_anim,R.anim.exit_anim);

    }

    private void initFrame() {
        ivFrame.setImageResource(R.drawable.animation_like);
        animDrawable = (AnimationDrawable) ivFrame.getDrawable();
        //动态添加新的item
        animDrawable.addFrame(getResources().getDrawable(R.drawable.ic_music), 900);
        animDrawable.addFrame(getResources().getDrawable(R.drawable.ic_status_bar_play_dark), 900);
        animDrawable.setOneShot(false);//是否只执行1次
        animDrawable.start();
    }

    public void onAnimBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_anim_frame:
                if (animDrawable.isRunning()) {
                    animDrawable.stop();
                } else {
                    animDrawable.run();
                }
                break;
            case R.id.btn_anim_tween_xml:
                //使用xml中的
//                ivFrame.startAnimation(translateAnimation);
//                ivFrame.startAnimation(scaleAnimation);
//                ivFrame.startAnimation(rotateAnimation);
//                ivFrame.startAnimation(alphaAnimation);
                ivFrame.startAnimation(setAnimation);
                break;
            case R.id.btn_anim_tween_java:
                //使用java中的
//                ivFrame.startAnimation(translateAnimation2);
//                ivFrame.startAnimation(scaleAnimation2);
//                ivFrame.startAnimation(rotateAnimation2);
//                ivFrame.startAnimation(alphaAnimation2);
                ivFrame.startAnimation(setAnimation2);
                break;
            case R.id.btn_anim_tween_custom:
                ivFrame.post(new Runnable() {
                    @Override
                    public void run() {
                        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0f, 360f,ivFrame.getWidth(),0,0,true );
                        rotate3dAnimation.setDuration(3000);
                        ivFrame.startAnimation(rotate3dAnimation);
                    }
                });
                break;
            case R.id.btn_anim_value_ofInt:
                // 启动动画
                valueAnimOfInt.start();
                break;
            case R.id.btn_anim_value_ofObject:
                // 启动动画
                valueAnimOfObject.start();
                break;
            case R.id.btn_anim_obj:
                // 启动动画
                objAnimAlpha.start();
                objAnimRotation.start();
                objAnimTrans.start();
                objAnimScale.start();
                break;
            case R.id.btn_anim_obj_set:
                AnimatorSet set=new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(ivFrame,"rotationX",0,360),
                        ObjectAnimator.ofFloat(ivFrame,"rotationY",0,180),
                        ObjectAnimator.ofFloat(ivFrame,"rotation",0,90),
                        ObjectAnimator.ofFloat(ivFrame,"translationX",0,90),
                        ObjectAnimator.ofFloat(ivFrame,"translationY",0,90),
                        ObjectAnimator.ofFloat(ivFrame,"scaleX",1,1.5f),
                        ObjectAnimator.ofFloat(ivFrame,"scaleY",1,0.5f),
                        ObjectAnimator.ofFloat(ivFrame,"alpha",1,0.1f,1)
                );
                set.setDuration(5*1000).start();
                break;
            default:
                break;
        }
    }

    public class Rotate3dAnimation extends Animation {
        private float mFromDegrees;
        private float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private float mDepthZ;
        private boolean mReverse;
        private Camera mCamera;

        public Rotate3dAnimation(float mFromDegrees, float mToDegrees, float mCenterX, float mCenterY, float mDepthZ, boolean mReverse) {
            this.mFromDegrees = mFromDegrees;
            this.mToDegrees = mToDegrees;
            this.mCenterX = mCenterX;
            this.mCenterY = mCenterY;
            this.mDepthZ = mDepthZ;
            this.mReverse = mReverse;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            super.applyTransformation(interpolatedTime, t);
            float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
            float centerX = mCenterX;
            float centerY = mCenterY;
            Camera camera = mCamera;
            Matrix matrix = t.getMatrix();
            camera.save();
            if (mReverse) {
//                camera.translate，这个方法接受3个参数，分别是x,y,z三个轴的偏移量，我们这里只将z轴进行了偏移，
                camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
            } else {
                camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
            }
            camera.rotateY(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
