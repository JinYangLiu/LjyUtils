package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ljy.ljyutils.R;

/**
 * Constraint:
 * 约束；限制；限定；严管；
 * ConstraintLayout:
 * 1. Android studio 的版本必须是2.3以上的才可以使用这个布局控件
 * 2. 子控件默认在左上角
 * 3. 子控件常用属性
 * app:layout_constraintTop_toTopOf="parent"
 * 自己的顶部依赖父布局的顶部
 * app:layout_constraintBottom_toBottomOf="parent"
 * app:layout_constraintLeft_toLeftOf="parent"
 * app:layout_constraintRight_toRightOf="parent"
 * app:layout_constraintRight_toLeftOf="parent"
 * ...
 * 确实很好用啊，难怪现在创建activity时默认是用这个跟布局
 * 比如下面这两行就可以实现水平居中：
 * app:layout_constraintTop_toTopOf="@+id/root"
 * app:layout_constraintBottom_toBottomOf="@+id/root"
 * 设置长宽比：
 * app:layout_constraintDimensionRatio="10:5"
 * 这个宽高比属性，还支持这样的写法：
 * app:layout_constraintDimensionRatio="W,16:6"
 * app:layout_constraintDimensionRatio="H,16:6"
 * <p>
 * 正常情况我们可以通过margin来设置与右侧与底部的距离。
 * 但是这里我们尝试使用量个新的属性：
 * layout_constraintHorizontal_bias
 * layout_constraintVertical_bias
 * <p>
 * Guideline：辅助线,该布局是不会显示到界面上的,可以用于推拽布局时做辅助使用
 * android:orientation取值为”vertical”和”horizontal”.
 * layout_constraintGuide_begin
 * layout_constraintGuide_end
 * layout_constraintGuide_percent
 * <p>
 * 性能比较结果表明：ConstraintLayout 在测量/布局阶段的性能比 RelativeLayout大约高 40%：
 */
public class ConstraintLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);
    }
}
