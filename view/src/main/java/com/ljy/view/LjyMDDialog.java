package com.ljy.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ljy.ljyview.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr.LJY on 2018/1/8.
 */
public class LjyMDDialog {

    private Activity mActivity;
    private AlertDialog mAlertDialog;

    public LjyMDDialog(Activity activity) {
        mActivity = activity;
    }

    /**
     * 两个按钮的
     *
     * @param title            标题
     * @param message          内容
     * @param positiveText     确定按钮内容
     * @param positiveListener 确定按钮监听事件
     * @param negativeText     取消按钮内容
     * @param negativeListener 取消按钮监听事件
     * @param isCancle         点击外部是否消失
     */
    public void alert(final String title,
                      final CharSequence message,
                      final String positiveText,
                      final PositiveListener positiveListener,
                      final String negativeText,
                      final NegativeListener negativeListener,
                      final boolean isCancle) {
        dismissProgressDialog();

        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppCompatAlertDialogStyle);

                    //标题
                    if (!TextUtils.isEmpty(title))
                        builder.setTitle(title);

                    //自定义内容
                    if (!TextUtils.isEmpty(message)) {
                        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_content, null);
                        final TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
                        tvContent.setText(message);
                        builder.setView(view);
//                        builder.setMessage(message);
                    }

                    //确定按钮
                    if (!TextUtils.isEmpty(positiveText)) {
                        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (positiveListener != null)
                                    positiveListener.positive();
                            }
                        });
                    }
                    //取消按钮
                    if (!TextUtils.isEmpty(negativeText)) {
                        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (negativeListener != null)
                                    negativeListener.negative();
                            }
                        });
                    }

                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancle);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                        initWidth(0);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }

                }
            });
        }

    }

    /**
     * 只显示一个按钮的
     *
     * @param title
     * @param message
     * @param positiveText
     * @param positiveListener
     * @param isCancle
     */
    public void alertSingleButton(final String title,
                                  final CharSequence message,
                                  final String positiveText,
                                  final PositiveListener positiveListener,
                                  final boolean isCancle) {
        alert(title, message, positiveText, positiveListener, null, null, isCancle);
    }


    /**
     * @param title            标题
     * @param hint             hint提示文字
     * @param info             下面的提示文字
     * @param positiveText     确定文字
     * @param positiveListener 确定监听
     * @param negativeText     取消文字
     * @param negativeListener 取消监听
     * @param maxLength        最大长度，默认0不设置
     * @param inputType        输入形式，默认0不设置
     * @param isCancle         点击外部是否消失
     *                         editText默认获取焦点并弹出键盘
     */
    public void alertEditTextMD(final String title, final String hint, final String info,
                                final String positiveText,
                                final PositiveListenerText positiveListener,
                                final String negativeText,
                                final NegativeListenerText negativeListener,
                                final int maxLength,
                                final int inputType,
                                final boolean isCancle) {
        alertEditTextMD(title, hint, info, positiveText, positiveListener, negativeText, negativeListener, maxLength, inputType, isCancle, true);
    }

    /**
     * @param title            标题
     * @param hint             hint提示文字
     * @param info             下面的提示文字
     * @param positiveText     确定文字
     * @param positiveListener 确定监听
     * @param negativeText     取消文字
     * @param negativeListener 取消监听
     * @param maxLength        最大长度，默认0不设置
     * @param inputType        输入形式，默认0不设置
     * @param isCancle         点击外部是否消失
     * @param isFocusable      editText是否获取焦点并弹出键盘
     */
    public void alertEditTextMD(final String title, final String hint, final String info,
                                final String positiveText,
                                final PositiveListenerText positiveListener,
                                final String negativeText,
                                final NegativeListenerText negativeListener,
                                final int maxLength,
                                final int inputType,
                                final boolean isCancle, final boolean isFocusable) {
        dismissProgressDialog();
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppCompatAlertDialogStyle);
                    //标题
                    if (!TextUtils.isEmpty(title))
                        builder.setTitle(title);
                    //自定义内容
                    View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_edittext_md, null);
                    TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
                    TextView textViewInfo = (TextView) view.findViewById(R.id.info);
                    final EditText inputText = textInputLayout.getEditText();
                    if (!TextUtils.isEmpty(hint))
                        textInputLayout.setHint(hint);
                    builder.setView(view);

                    //设置下面的提示文字
                    if (TextUtils.isEmpty(info)) {
                        textViewInfo.setVisibility(View.GONE);
                    } else {
                        textViewInfo.setVisibility(View.VISIBLE);
                        textViewInfo.setText(info);
                    }
                    //设置最大支持长度
                    if (maxLength != 0) {
                        inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                    }
                    //设置输入类型
                    if (inputType != 0)
                        inputText.setInputType(inputType);

                    //确定按钮
                    if (!TextUtils.isEmpty(positiveText)) {
                        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = inputText.getText().toString().trim();
                                dialog.dismiss();
                                if (positiveListener != null) {
                                    positiveListener.positive(text);
                                }

                            }
                        });
                    }
                    //取消按钮
                    if (!TextUtils.isEmpty(negativeText)) {
                        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = inputText.getText().toString().trim();
                                dialog.dismiss();
                                if (negativeListener != null)
                                    negativeListener.negative(text);
                            }
                        });
                    }
                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancle);
                        mAlertDialog = builder.create();

                        if (isFocusable) {
                            //弹出软键盘
                            openKeyboard();
                        } else {
                            //隐藏键盘
                            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                            view.setFocusableInTouchMode(true);
                            view.setFocusable(true);
                        }
                        mAlertDialog.show();
                        initWidth(0);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }

                }
            });
        }
    }

    /**
     * 打开软键盘
     */
    private void openKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);
    }



    public interface PositiveListener {
        public void positive();
    }

    public interface NegativeListener {
        public void negative();
    }

    public interface ItemSelectListener {
        void itemSelect(int which);
    }

    public interface PositiveListenerText {
        public void positive(String text);
    }

    public interface NegativeListenerText {
        public void negative(String text);
    }

    /**
     * 防止dialog多次弹出
     */
    public void dismissProgressDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mAlertDialog != null && mAlertDialog.isShowing() && !mActivity.isFinishing()) {
                    try {
                        mAlertDialog.dismiss();
                    } catch (Exception e) {
                    } finally {
                        mAlertDialog = null;
                    }
                }
            }
        });
    }

    /**
     * 设置弹窗的宽度
     * 占屏幕宽度的0.8
     * 背景设置0.1的黑色值
     */
    private void initWidth(int height) {

        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = mAlertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.3);//高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);//宽度设置为屏幕的0.8
        if (height > 0) {
            p.height = (int) (d.getHeight() * 0.6);//高度设置为屏幕的0.6
        }

        p.dimAmount = 0.1f;
        mAlertDialog.getWindow().setAttributes(p);//设置生效

    }

    /**
     * 设置dialog宽度，需要在show之后调用
     * @param activity
     * @param dialog
     */
    public static void initWidth(Activity activity, AlertDialog dialog) {
        initWidth(activity,dialog,0.8f);// 宽度设置为屏幕的0.8
    }
    public static void initWidth(Activity activity, AlertDialog dialog, float scale) {
        Window window = dialog.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * scale); //设置宽度
        p.dimAmount = (1f-scale)/2f;
        window.setAttributes(p);
    }


    /**
     * Dialog是否处于显示状态
     *
     * @return true:显示
     */
    public boolean dialogIsShow() {
        if (mAlertDialog != null) {
            return mAlertDialog.isShowing();
        } else {
            return false;
        }
    }

}
