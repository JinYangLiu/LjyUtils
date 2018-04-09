package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ljy.CustomAnnotation;
import com.ljy.CustomClass;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.annotation.AnnotationActivityUtils;
import com.ljy.ljyutils.annotation.LBindUtils;
import com.ljy.ljyutils.annotation.LBindView;
import com.ljy.ljyutils.annotation.MethodInfo;
import com.ljy.ljyutils.annotation.MethodInfoUtil;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

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
public class AnnotationActivity extends BaseActivity {

    @LBindView(R.id.text_info)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        LjyLogUtil.setAppendLogMsg(true);

        LBindUtils.bind(mActivity);
        MethodInfoUtil.showInfo(this);
        LjyLogUtil.i("-----------------------");
        AnnotationActivityUtils.method002(this);

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



}
