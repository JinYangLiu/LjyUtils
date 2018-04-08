package com.ljy.ljyutils.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyDensityUtil;
import com.ljy.view.LjyDoodleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoodleActivity extends BaseActivity {

    @BindView(R.id.doodleView)
    LjyDoodleView mDoodleView;
    private AlertDialog mColorDialog;
    private AlertDialog mPaintDialog;
    private AlertDialog mShapeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);
        ButterKnife.bind(mActivity);
        mDoodleView.setSize(LjyDensityUtil.dp2px(mContext, 5));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //在此调用修改字体工具类的方法
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDoodleView.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doodle_view, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_color:
                showColorDialog();
                break;
            case R.id.main_size:
                showSizeDialog();
                break;
            case R.id.main_action:
                showShapeDialog();
                break;
            case R.id.main_reset:
                mDoodleView.reset();
                break;
            case R.id.main_save:
                String path = mDoodleView.saveBitmap(mDoodleView);
                Toast.makeText(this, "保存图片的路径为：" + path, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 显示选择画笔颜色的对话框
     */
    private void showColorDialog() {
        if (mColorDialog == null) {
            mColorDialog = new AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(new String[]{"蓝色", "红色", "绿色"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setColor(ContextCompat.getColor(mContext, R.color.thin_blue));
                                            break;
                                        case 1:
                                            mDoodleView.setColor(ContextCompat.getColor(mContext, R.color.thin_red));
                                            break;
                                        case 2:
                                            mDoodleView.setColor(ContextCompat.getColor(mContext, R.color.thin_green));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mColorDialog.show();
    }

    /**
     * 显示选择画笔粗细的对话框
     */
    private void showSizeDialog() {
        if (mPaintDialog == null) {
            mPaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setSize(dip2px(5));
                                            break;
                                        case 1:
                                            mDoodleView.setSize(dip2px(10));
                                            break;
                                        case 2:
                                            mDoodleView.setSize(dip2px(15));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mPaintDialog.show();
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"路径", "直线", "矩形", "圆形", "实心矩形", "实心圆"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setType(LjyDoodleView.ActionType.Path);
                                            break;
                                        case 1:
                                            mDoodleView.setType(LjyDoodleView.ActionType.Line);
                                            break;
                                        case 2:
                                            mDoodleView.setType(LjyDoodleView.ActionType.Rect);
                                            break;
                                        case 3:
                                            mDoodleView.setType(LjyDoodleView.ActionType.Circle);
                                            break;
                                        case 4:
                                            mDoodleView.setType(LjyDoodleView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            mDoodleView.setType(LjyDoodleView.ActionType.FilledCircle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
