package com.ljy.ljyutils.annotation;

import android.annotation.SuppressLint;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

/**
 * Created by Mr.LJY on 2018/4/8.
 * <p>
 * 对自定义注解的处理
 */

public class LBindUtils {
    @SuppressLint("WrongConstant")
    public static void bind(Object target) {

        if (target == null)
            return;

        Class<?> targetClass = target.getClass();

        if (targetClass == null)
            return;

        //LBindLayout
        if (targetClass.isAnnotationPresent(LBindLayout.class)) {
            LBindLayout lBindLayout = targetClass.getAnnotation(LBindLayout.class);
            if (lBindLayout != null) {
                // 获取注解中的对应的布局id 因为注解只有个方法 所以@XXX(YYY)时会自动赋值给注解类唯一的方法
                int layoutResId = lBindLayout.value();
                try {
                    // 得到activity中的方法 第一个参数为方法名 第二个为可变参数 类型为 参数类型的字节码
                    Method method = targetClass.getMethod("setContentView", int.class);
                    // 调用方法 第一个参数为哪个实例去调用 第二个参数为 参数
                    method.invoke(target, layoutResId);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

        //LBindView
        Field[] fields = targetClass.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(LBindView.class)) {
                    LBindView lBindView = field.getAnnotation(LBindView.class);
                    if (lBindView != null) {
                        int viewResId = lBindView.value();
                        try {
//                            field.set(target, target.findViewById(viewResId));
                            Method method = targetClass.getMethod("findViewById", int.class);
                            Object obj = method.invoke(target, viewResId);
                            //如 private TextView tv 这种情况 如果不打破封装会直接异常
                            if (Modifier.PUBLIC != field.getModifiers()) {
                                // 打破封装性
                                field.setAccessible(true);
                            }
                            field.set(target, obj);//相当于 View v = findViewById(...);

                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //LBindOnClick
        Method[] methods=targetClass.getMethods();
        if (methods!=null){
            for (Method method:methods){
                if (method.isAnnotationPresent(LBindOnClick.class)){
                    LBindOnClick lBindOnClick=method.getAnnotation(LBindOnClick.class);
                    // 如果存在此注解
                    if (lBindOnClick!=null){
                        int[] ids=lBindOnClick.value();
                        //代理处理类
                        MyInvocationHandler handler=new MyInvocationHandler(target,method);
                        // 代理实例这里也可以返回 new Class<?>[] { View.OnClickListener.class }中的接口类
                        // 第一个参数用于加载其他类 不一定要使用View.OnClickListener.class.getClassLoader() 你可以使用其他的
                        // 第二个参数你所实现的接口
                        Object proxyInstance= Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(),
                                new Class<?>[]{View.OnClickListener.class},handler);
                        // 遍历所有的控件id 然后设置代理
                        for (int id:ids){
                            try {
                                Method methodFindViewById=targetClass.getMethod("findViewById",int.class);
                                Object view=methodFindViewById.invoke(target,id);
                                if (view!=null){
                                    Method methodSetOnClickListener=view.getClass().getMethod("setOnClickListener",View.OnClickListener.class);
                                    methodSetOnClickListener.invoke(view,proxyInstance);
                                }
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }
    }


    static class MyInvocationHandler implements InvocationHandler{

        //这里我们到时候回传入activity
        private Object target;
        //用户自定义view的点击事件方法
        private Method method;

        public MyInvocationHandler(Object target, Method method) {
            super();
            this.target = target;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.method.invoke(target,args);
        }
    }
}
