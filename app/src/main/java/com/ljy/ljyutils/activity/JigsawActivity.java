package com.ljy.ljyutils.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.view.jigsaw.LjyJigsawView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 拼图
 */
public class JigsawActivity extends BaseActivity {

    @BindView(R.id.tv_level)
    TextView tv_level;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.ljyJigsawView)
    LjyJigsawView ljyJigsawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        ButterKnife.bind(mActivity);
        //显示时间
        ljyJigsawView.setTimeEnabled(true);

        ljyJigsawView.setOnGamemListener(new LjyJigsawView.GamePintuListener() {
            @Override
            public void nextLevel(final int nextLevel) {

                new AlertDialog.Builder(mActivity).setTitle("拼图完成").setMessage("美女抱回家").setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ljyJigsawView.nextLevel();
                        tv_level.setText("当前关卡" + nextLevel);
                    }
                }).show();
            }

            @Override
            public void timechanged(int time) {
                //设置时间
                tv_time.setText("倒计时：" + time);
            }

            @Override
            public void gameOver() {
                new AlertDialog.Builder(mActivity).setTitle("游戏结束").setMessage("很遗憾没有成功抱到美女！").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ljyJigsawView.restartGame();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        ljyJigsawView.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ljyJigsawView.resumeGame();
    }
}
