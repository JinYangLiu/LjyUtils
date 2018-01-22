package com.ljy.ljyutils.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.view.LjyRadarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Radar2Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.radarView)
    LjyRadarView radarView;
    @BindView(R.id.countTextView)
    TextView countTextView;
    @BindView(R.id.layerCountTextView)
    TextView layerCountTextView;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.seekBar2)
    SeekBar seekBar2;
    @BindView(R.id.seekBar3)
    SeekBar seekBar3;
    @BindView(R.id.seekBar4)
    SeekBar seekBar4;
    @BindView(R.id.drawBorder)
    ToggleButton drawBorder;
    @BindView(R.id.drawPoint)
    ToggleButton drawPoint;
    @BindView(R.id.drawPolygon)
    ToggleButton drawPolygon;
    @BindView(R.id.drawShade)
    ToggleButton drawShade;
    @BindView(R.id.drawMultiColor)
    ToggleButton drawMultiColor;
    @BindView(R.id.drawRadius)
    ToggleButton drawRadius;
    @BindView(R.id.drawText)
    ToggleButton drawText;
    @BindView(R.id.drawIcon)
    ToggleButton drawIcon;
    @BindView(R.id.drawRichText)
    ToggleButton drawRichText;
    @BindView(R.id.loadAnimation)
    Button loadAnimation;

    // 图标
    private int drawables[] = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    // 标题
    CharSequence titles[] = new CharSequence[]{"击杀", "金钱", "生存", "防御", "魔方", "物理", "助攻", "智慧"};
    // 每种属性的值（0~1.0）
    double percents[] = new double[]{0.8, 0.8, 0.9, 1, 0.6, 0.5, 0.77, 0.9};
    // 各个标题下面的数值文本
    CharSequence values[] = new CharSequence[]{"80", "80%", "0.9", "100%", "3/5", "0.5", "0.77个", "1~12"};
    // 用不同颜色区别相邻区域时的颜色数组（可选）
    int colors[] = new int[]{Color.parseColor("#A0ffcc00"), Color.parseColor("#A000ff00"),
            Color.parseColor("#A00000ff"), Color.parseColor("#A0FF00FF"), Color.parseColor("#A000FFFF"),
            Color.parseColor("#A0FFFF00"), Color.parseColor("#A000FF00"), Color.parseColor("#A0FF00FF")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar2);
        ButterKnife.bind(mActivity);

        drawBorder.setOnCheckedChangeListener(this);
        drawPoint.setOnCheckedChangeListener(this);
        drawPolygon.setOnCheckedChangeListener(this);
        drawShade.setOnCheckedChangeListener(this);
        drawMultiColor.setOnCheckedChangeListener(this);
        drawRadius.setOnCheckedChangeListener(this);
        drawText.setOnCheckedChangeListener(this);
        drawIcon.setOnCheckedChangeListener(this);
        drawRichText.setOnCheckedChangeListener(this);

        loadAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.loadAnimation(true);
            }
        });

        // 雷达图各项值比例
        radarView.setPercents(percents);
        // 设置各区域颜色的数组
        // 如果设置了colors则会每个区域显示不同的颜色，否则所有区域显示同一个颜色   操作1
        radarView.setColors(colors);
        // 如果没有想设置所有区域同一种颜色，可以设置dataColor                    操作2    （操作1和操作2 互斥）
//        radarView.setDataColor(Color.parseColor("#999900"));
        // 设置各项标题
        radarView.setTitles(titles);
        // 设置各项显示的值的文本
        radarView.setValues(values);
        // 设置各项的图标
        radarView.setDrawables(drawables);
        // 设置允许各个点之间连线
        radarView.setEnabledBorder(true);
        // 显示圆点
        radarView.setEnabledShowPoint(true);
        // 绘制正n变形
        radarView.setEnabledPolygon(true);
        // 绘制渐变环
        radarView.setEnabledShade(true);
        // 绘制半径
        radarView.setEnabledRadius(true);
        // 绘制标题，数值和图标等
        radarView.setEnabledText(true);
        // 开启动画
        radarView.setEnabledAnimation(true);
        // 设置层数
        radarView.setLayerCount(5);
        // 无渐变色的单一色
        radarView.setSingleColor(Color.parseColor("#800000ff"));

        // 雷达图标题和图标的点击事件
        radarView.setOnTitleClickListener(new LjyRadarView.OnTitleClickListener() {
            @Override
            public void onTitleClick(LjyRadarView view, int position, int x, int y, Rect rect) {
                Toast.makeText(Radar2Activity.this, "position = " + position, Toast.LENGTH_SHORT).show();
                Log.d("lxc", "position ----> " + position);
                Log.d("lxc", "x ----> " + x);
                Log.d("lxc", "y ----> " + y);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String jibianxing = "";
                if (progress == 0) {
                    jibianxing = "正三边形";
                } else if (progress == 1) {
                    jibianxing = "正四边形";
                } else if (progress == 2) {
                    jibianxing = "正五边形";
                } else if (progress == 3) {
                    jibianxing = "正六边形";
                } else if (progress == 4) {
                    jibianxing = "正七边形";
                }
                countTextView.setText(jibianxing);
                radarView.setCount(progress + 3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String layoutText = "层数";
                layerCountTextView.setText(layoutText + progress);
                radarView.setLayerCount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radarView.setDrawableSize(progress + 20);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radarView.setTitleSize(progress + 30);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });




    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.drawBorder:
                radarView.setEnabledBorder(isChecked);
                break;
            case R.id.drawPoint:
                radarView.setEnabledShowPoint(isChecked);
                break;
            case R.id.drawPolygon:
                radarView.setEnabledPolygon(isChecked);
                break;
            case R.id.drawShade:
                radarView.setEnabledShade(isChecked);
                break;
            case R.id.drawMultiColor:
                if (isChecked) {
                    radarView.setColors(colors);
                } else {
                    radarView.setColors(null);
                }
                break;
            case R.id.drawRadius:
                radarView.setEnabledRadius(isChecked);
                break;
            case R.id.drawText:
                radarView.setEnabledText(isChecked);
                break;
            case R.id.drawIcon:
                if (isChecked) {
                    radarView.setDrawables(drawables);
                } else {
                    radarView.setDrawables(null);
                }
                break;
            case R.id.drawRichText:
                if (isChecked) {
                    // 注意，如果使用富文本，则只显示titles字段了，所以可以手动将values拼接到titles后面
                    SpannableString ss = new SpannableString("富文本\n值为0.1");
                    ss.setSpan(new AbsoluteSizeSpan(30), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.GREEN), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new BackgroundColorSpan(Color.YELLOW), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan(10), 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    titles[0] = ss;
                    radarView.setTitles(titles);
                } else {
                    titles[0] = "击杀";
                    radarView.setTitles(titles);
                }
                break;
        }
    }
}
