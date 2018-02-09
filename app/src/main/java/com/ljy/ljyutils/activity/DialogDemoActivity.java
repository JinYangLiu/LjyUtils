package com.ljy.ljyutils.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyMDDialogManager;

public class DialogDemoActivity extends BaseActivity {

    private LjyMDDialogManager dialogManager;
    private ProgressDialog waitingDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        dialogManager = new LjyMDDialogManager(mActivity);
    }

    public void onDialogBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_normal:
                showNormalDialog();
                break;
            case R.id.btn_neutralBtn:
                showNeutralBtnDialog();
                break;
            case R.id.btn_singleBtn:
                showSingleBtnDialog();
                break;
            case R.id.btn_editable:
                showEditableDialog();
                break;
            case R.id.btn_singleEditable:
                showSingleEditableDialog();
                break;
            case R.id.btn_list:
                showListDialog();
                break;
            case R.id.btn_singleChoice:
                showSingleChoiceDialog();
                break;
            case R.id.btn_multiChoice:
                showMultiChoiceDialog();
                break;
            case R.id.btn_waitingShow:
                showWaitingDialog();
                break;
            case R.id.btn_progressShow:
                showProgressDialog();
                break;
            default:
                break;

        }
    }


    private void showProgressDialog() {
        final int max = 20;
        if (progressDialog == null)
            progressDialog = LjyMDDialogManager.getProgressDialog(mActivity, "标题啊~", max, true);
        if (!progressDialog.isShowing())
            progressDialog.show();

        /**
         * 模拟进度增加的过程
         * 新开一个线程，每个100ms，进度增加1
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < max) {
                    try {
                        Thread.sleep(100);
                        progress++;
                        progressDialog.setProgress(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private void showWaitingDialog() {
        if (waitingDialog == null)
            waitingDialog = LjyMDDialogManager.getWaitingDialog(mActivity, null, "内容哦~~", true);
        if (!waitingDialog.isShowing())
            waitingDialog.show();
    }

    private void showMultiChoiceDialog() {
        final String[] items = {"0000", "1111", "2222", "3333", "4444", "5555"};
        final boolean[] initChoiceSets = {true, false, true, false, false, false};
        dialogManager.alertMultiChoice("列表的标题啊", items, initChoiceSets, new LjyMDDialogManager.OnItemChecked() {
            @Override
            public void item(boolean[] choiceSets) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < choiceSets.length; i++) {
                    if (choiceSets[i]) {
                        stringBuffer.append(items[i]);
                        stringBuffer.append(",");
                    }
                }
                LjyToastUtil.toast(mContext, "选择了：" + stringBuffer.toString());
            }

        }, false);
    }

    private void showSingleChoiceDialog() {
        final String[] items = {"0000", "1111", "2222", "3333", "4444", "5555"};
        dialogManager.alertSingleChoice("列表的标题啊", 2, items, new LjyMDDialogManager.OnItemListener() {
            @Override
            public void item(int index) {
                LjyToastUtil.toast(mContext, "选择了：" + items[index]);
            }
        }, false);
    }

    private void showListDialog() {
        final String[] items = {"1111", "2222", "3333", "4444", "5555"};
        dialogManager.alertList("列表的标题啊", items, new LjyMDDialogManager.OnItemListener() {
            @Override
            public void item(int index) {
                LjyToastUtil.toast(mContext, "点击了：" + items[index]);
            }
        }, false);
    }

    private void showNeutralBtnDialog() {
        dialogManager.alertNeutralButton("我是标题啊", "我是内容啊~~~是内容啊~~~内容啊~~~容啊~~~啊~~~",
                "確定", new LjyMDDialogManager.OnNeutralListener() {
                    @Override
                    public void neutral() {
                        LjyToastUtil.toast(mContext, "確定的回調執行了");
                    }
                }, true);
    }

    private void showSingleEditableDialog() {
        dialogManager.alertEditTextMD(null, "请输入内容吧~~", null,
                "確定", new LjyMDDialogManager.PositiveListenerText() {
                    @Override
                    public void positive(String text) {
                        LjyToastUtil.toast(mContext, "点击了确定，输入：" + text);
                    }
                }, null, null, 8, InputType.TYPE_CLASS_NUMBER, true, false);
    }

    private void showEditableDialog() {
        dialogManager.alertEditTextMD("请输入内容吧~~", new LjyMDDialogManager.PositiveListenerText() {
            @Override
            public void positive(String text) {
                LjyToastUtil.toast(mContext, "点击了确定，输入：" + text);
            }
        }, new LjyMDDialogManager.NegativeListenerText() {
            @Override
            public void negative(String text) {
                LjyToastUtil.toast(mContext, "点击了取消，输入：" + text);
            }
        });
    }

    private void showSingleBtnDialog() {
        dialogManager.alertSingleButton("我是标题啊", "我是内容啊~~~是内容啊~~~内容啊~~~容啊~~~啊~~~",
                "確定", new LjyMDDialogManager.OnPositiveListener() {
                    @Override
                    public void positive() {
                        LjyToastUtil.toast(mContext, "確定的回調執行了");
                    }
                }, true);
    }

    private void showNormalDialog() {
        dialogManager.alertTwoButton("我是标题啊", "我是内容啊~~~是内容啊~~~内容啊~~~容啊~~~啊~~~",
                "確定", new LjyMDDialogManager.OnPositiveListener() {
                    @Override
                    public void positive() {
                        LjyToastUtil.toast(mContext, "確定的回調執行了");
                    }
                }, "取消", new LjyMDDialogManager.OnNegativeListener() {
                    @Override
                    public void negative() {
                        LjyToastUtil.toast(mContext, "取消的回調執行了");
                    }
                }, true);
    }
}
