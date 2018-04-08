package com.ljy.ljyutils.test;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyReflectUtils;
import com.ljy.util.LjyToastUtil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Mr.LJY on 2018/4/3.
 * <p>
 * AspectJ在Android中的强势插入
 * <p>
 * AOP是Aspect Oriented Programming的缩写，即『面向切面编程』
 * <p>
 * 在不侵入原有代码的基础上，增加新的代码
 */
@Aspect
public class AspectTest {
    /*
    //1. @Before : Advice,具体的插入点
    //2. execution : 处理Join Point的类型
    //3. (android.app.Activity.on*(..))：这个是最重要的表达式，第一个『*』表示返回值，
    // 『*』表示返回值为任意类型，后面这个就是典型的包名路径，其中可以包含『*』来进行通配，
    // 几个『*』没区别。同时，这里可以通过『&&、||、!』来进行条件组合。()代表这个方法的参数，
    // 你可以指定类型，例如android.os.Bundle，或者(..)这样来代表任意类型、任意个数的参数
    @Before("execution(* android.app.Activity.on**(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) {
        //实际切入的代码
        String key = joinPoint.getSignature().toString();
        LjyLogUtil.i("---> onActivityMethodBefore:" + key);
    }
    */

    @Before("execution(* com.ljy.ljyutils.activity.AspectJTestActivity.methodTest001(..))")
    public void onBtnClickBefore(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        LjyLogUtil.i("onBtnClickBefore: to do...");
    }

    @After("execution(* com.ljy.ljyutils.activity.AspectJTestActivity.methodTest002(..))")
    public void onBtnClickAfter(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        LjyLogUtil.i("onBtnClickAfter: to do...");
    }

    //在方法前后各插入代码，包含了Before和After的全部功能
    //但是Around和After是不能同时作用在同一个方法上的，会产生重复切入的问题。
    @Around("execution(* com.ljy.ljyutils.activity.AspectJTestActivity.methodTest003(..))")
    public void onBtnClickAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String key = proceedingJoinPoint.getSignature().toString();
        LjyLogUtil.i("onBtnClickAround_first:  to do...");
        proceedingJoinPoint.proceed();//执行原始的方法
        LjyLogUtil.i("onBtnClickAround_second:  to do...");
    }

    //先定义Pointcut，并申明要监控的方法名，最后，在Before或者其它Advice里面添加切入代码，即可完成切入
    @Pointcut("execution(@com.ljy.ljyutils.test.DebugTool * *(..))")
    public void DebugToolMethod() {

    }

    @Before("DebugToolMethod()")
    public void onDebugToolMethodBefore(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        LjyLogUtil.i("onDebugToolMethodBefore:  to do...");
    }

    //call和execution
    //在AspectJ的切入点表达式中，我们前面都是使用的execution，实际上，还有一种类型——call
    /*
    对照起来看就一目了然了，execution是在被切入的方法中，call是在调用被切入的方法前或者后。

    对于Call来说：
    Call（Before）
    Pointcut{
        Pointcut Method
    }
    Call（After）

    对于Execution来说：
    Pointcut{
      execution（Before）
        Pointcut Method
      execution（After）
    }
     */
    @Around("call(* com.ljy.ljyutils.activity.AspectJTestActivity.methodTest005(..))")
    public void onBtnClickAroundCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String key = proceedingJoinPoint.getSignature().toString();
        LjyLogUtil.i("onBtnClickAround_call_first:  to do...");
        proceedingJoinPoint.proceed();//执行原始的方法
        LjyLogUtil.i("onBtnClickAround_call_second:  to do...");
    }

    //除了前面提到的call和execution，比较常用的还有一个withincode。
    // 这个语法通常来进行一些切入点条件的过滤，作更加精确的切入控制
    @Pointcut("withincode(* com.ljy.ljyutils.activity.AspectJTestActivity.testAOP2(..))")
    public void invokeTestAOP2() {

    }

    @Pointcut("call(* com.ljy.ljyutils.activity.AspectJTestActivity.methodTest006(..))")
    public void invokeMethodTest006() {

    }

    // 同时满足前面的条件，即在testAOP2()方法内调用methodTest006()方法的时候才切入
    @Pointcut("invokeMethodTest006() && invokeTestAOP2()")
    public void invokeMethodTest006OnlyTestAOP2() {

    }

    @Before("invokeMethodTest006OnlyTestAOP2()")
    public void beforeInvokeMethodTest006OnlyTestAOP2(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        LjyLogUtil.i("beforeInvokeMethodTest006OnlyTestAOP2: to do...");
    }

    @AfterThrowing(pointcut = "execution(* com.ljy.ljyutils.*.*(..))", throwing = "exception")
    public void catchExceptionMethod(Exception exception) {
        String msg = exception.getLocalizedMessage();
        LjyLogUtil.i("catchExceptionMethod:" + msg);
    }

    @Pointcut("call(* android.widget.Toast+.show(..))")
    public void toastShow() {

    }

    @Around("toastShow()")
    public void toastShow(ProceedingJoinPoint point) {
        Toast toast = (Toast) point.getTarget();
        try {
            Context context = (Context) LjyReflectUtils.getValue(toast, "mContext");
            if (Build.VERSION.SDK_INT >= 19 && NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                point.proceed();
            } else {
                LjyToastUtil.windowToast(context, "windowToast", 2000);
                LjyLogUtil.i("toastShow");
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


}
