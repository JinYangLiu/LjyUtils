package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljy.CustomAnnotation;
import com.ljy.CustomClass;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.annotation.AnnotationActivityUtils;
import com.ljy.ljyutils.annotation.LBindLayout;
import com.ljy.ljyutils.annotation.LBindOnClick;
import com.ljy.ljyutils.annotation.LBindUtils;
import com.ljy.ljyutils.annotation.LBindView;
import com.ljy.ljyutils.annotation.MethodInfo;
import com.ljy.ljyutils.annotation.MethodInfoUtil;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.Cat;
import com.ljy.ljyutils.bean.User;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Annotation and reflection
 * 注解与反射
 * <p>
 * 对于编译期来说,代码中的元素结构是基本不变的(包,类,函数,字段,类型参数,变量)
 * JDK中为这些元素定义了一个基类Element,其有以下几个子类:
 * PackageElement, 包元素,包含了某个包下的信息,可以获取到包名等
 * TypeElement, 类型元素,如某个字段属于某种类型
 * ExecutableElement, 可执行元素,代表了函数类型的元素
 * VariableElement, 变量元素
 * TypeParameterElement, 类型参数元素
 * <p>
 * Java内建注解
 * Java提供了三种内建注解。
 * 1. @Override
 * 当我们想要复写父类中的方法时，我们需要使用该注解去告知编译器我们想要复写这个方法。
 * 这样一来当父类中的方法移除或者发生更改时编译器将提示错误信息。
 * 2. @Deprecated
 * 当我们希望编译器知道某一方法不建议使用时，我们应该使用这个注解。Java在javadoc中推荐使用该注解，
 * 我们应该提供为什么该方法不推荐使用以及替代的方法。
 * 3. @SuppressWarnings
 * 这个仅仅是告诉编译器忽略特定的警告信息，例如在泛型中使用原生数据类型。
 * 它的保留策略是SOURCE（译者注：在源文件中有效）并且被编译器丢弃。
 */
@CustomClass("AnnotationActivity")
@LBindLayout(R.layout.activity_annotation)
public class AnnotationActivity extends BaseActivity {

    @LBindView(R.id.text_info)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_annotation);
        LjyLogUtil.setAppendLogMsg(true);

        LBindUtils.bind(mActivity);
        MethodInfoUtil.showInfo(this);
        LjyLogUtil.i("---------编译时生成代码(APT)--------------");
        AnnotationActivityUtils.method002(this);
        LjyLogUtil.i("-----------获取class对象的三种方法------------");
        getClassObj();
        LjyLogUtil.i("----------反射获取类的对象-------------");
        getObj();
        LjyLogUtil.i("-----------反射获取类的方法------------");
        getMothods();
        LjyLogUtil.i("------------反射获取类的属性-----------");
        getFields();
        LjyLogUtil.i("------------获取对象的父类-----------");
        getSuperClass();
        LjyLogUtil.i("------------获取对象实现的接口-----------");
        getInterfaces();

        mTextView.append(LjyLogUtil.getAllLogMsg());
        LjyLogUtil.setAppendLogMsg(false);
    }

    @MethodInfo(author = "ljy", comments = "我是method001啊", date = "2018-04-08", revision = 2)
    private void method001() {

    }

    @CustomAnnotation("AnnotationActivity")
    public void method002() {
        LjyLogUtil.i("AnnotationActivity.method002");
    }

    @LBindOnClick({R.id.btnTest, R.id.btnTest002})
    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                LjyToastUtil.showSnackBar(v, "点击了btnTest");
                break;
            case R.id.btnTest002:
                LjyToastUtil.showSnackBar(v, "点击了btnTest002啊~~");
                break;
        }
    }

    /**
     * 获取class对象的三种方法
     */
    public Class getClassObj() {
        // 1.根据类名获取Class对象
        Class clzz1 = Cat.class;
        // 2.根据对象获取Class对象
        Cat cat = new Cat();
        Class clzz2 = cat.getClass();
        // 3.根据完整类名获取Class对象
        try {
            Class clzz3 = Class.forName("com.ljy.ljyutils.bean.Cat");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        LjyLogUtil.i(clzz1.toString());
        return clzz1;//clzz2,clzz3
    }

    /**
     * 反射获取类的对象
     */
    public Object getObj() {
        //获取Class对象
        Class clzz = User.class;
        //获取类对象的Constructor (构造方法)
        try {
            Constructor constructor = clzz.getConstructor(String.class, String.class, String.class);
            // 在使用时取消 Java语言访问检查，提升反射性能
            constructor.setAccessible(true);
            // 通过 Constructor 来创建对象
            Object obj = constructor.newInstance("小明", "123123", "18");
            LjyLogUtil.i(obj.toString());
            return obj;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射获取类的方法
     */
    public void getMothods() {
        //获取Class对象
        User user = new User();
        Class clzz = user.getClass();
        // 获取到类中的所有方法(不包含从父类继承的方法)
        Method[] methods = clzz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            LjyLogUtil.i("method[" + i + "]:" + methods[i].getName());
        }
        try {
            // 获取类中的某个方法
            Method method = clzz.getMethod("setName", String.class);
            LjyLogUtil.i("method:" + method.getName());
            // 判断是否是public方法
            LjyLogUtil.i("method is public:" + Modifier.isPublic(method.getModifiers()));
            // 获取该方法的参数类型列表
            Class[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                LjyLogUtil.i("paramTypes[" + i + "]:" + paramTypes[i].getName());
            }
            LjyLogUtil.i("user.name before:" + user.getName());
            //设置方法值
            method.invoke(user, "小莉");
            LjyLogUtil.i("user.name after:" + user.getName());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 反射获取类的属性
     */
    public void getFields() {
        //获取Class对象
        User user = new User();
        Class clzz = user.getClass();
        // 获取当前类和父类的所有属性
        Field[] fields = clzz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            LjyLogUtil.i("fields[" + i + "]:" + fields[i].getName());
        }
        try {
            //获取某个属性
            Field field = clzz.getDeclaredField("name");
            LjyLogUtil.i("user.name before:" + field.get(user));
            //设置属性值
            field.set(user, "明明");
            LjyLogUtil.i("user.name after:" + field.get(user));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的父类
     */
    public void getSuperClass() {
        //获取Class对象
        User user = new User();
        Class clzz = user.getClass();
        //获取父类
        Class superClass = clzz.getSuperclass();
        // 循环获取上一层父类（如果存在）,至少存在一层java.lang.Object
        while (superClass != null) {
            LjyLogUtil.i("superClass:" + clzz.getSuperclass().getName());
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * 获取对象实现的接口
     */
    public void getInterfaces(){
        //获取Class对象
        User user = new User();
        Class clzz = user.getClass();
        //获取该类实现的所有接口
        Class[] interfaceClasses=clzz.getInterfaces();
        for (int i = 0; i < interfaceClasses.length; i++) {
            LjyLogUtil.i("interfaceClasses[" + i + "]:" + interfaceClasses[i].getName());
        }
    }

}
