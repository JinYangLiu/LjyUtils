package com.ljy.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ljy.lib.R;
import com.ljy.util.LjyScreenUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr.LJY on 2018/1/8.
 * <p>
 * MD风格的Dialog
 */
public class LjyMDDialogManager {

    private Activity mActivity;
    private AlertDialog mAlertDialog;

    public LjyMDDialogManager(Activity activity) {
        mActivity = activity;
    }

    /**
     * 两个按钮的
     *
     * @param title              标题
     * @param message            内容
     * @param positiveText       确定按钮内容
     * @param onPositiveListener 确定按钮监听事件
     * @param negativeText       取消按钮内容
     * @param onNegativeListener 取消按钮监听事件
     * @param neutralText        中立按钮内容
     * @param onNeutralListener  中立按钮监听事件
     * @param isCancelable       点击外部是否消失
     */
    public void alert(final String title,
                      final CharSequence message,
                      final String positiveText,
                      final OnPositiveListener onPositiveListener,
                      final String negativeText,
                      final OnNegativeListener onNegativeListener,
                      final String neutralText,
                      final OnNeutralListener onNeutralListener,
                      final boolean isCancelable) {
        dismissDialog();

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
                                if (onPositiveListener != null)
                                    onPositiveListener.positive();
                            }
                        });
                    }
                    //取消按钮
                    if (!TextUtils.isEmpty(negativeText)) {
                        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (onNegativeListener != null)
                                    onNegativeListener.negative();
                            }
                        });
                    }

                    if (!TextUtils.isEmpty(neutralText)) {
                        builder.setNeutralButton(neutralText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onNeutralListener.neutral();
                                    }
                                });
                    }

                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancelable);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                        initWidth(mActivity, mAlertDialog);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }

                }
            });
        }

    }

    /**
     * 只显示一个按钮的
     */
    public void alertSingleButton(final String title,
                                  final CharSequence message,
                                  final String positiveText,
                                  final OnPositiveListener onPositiveListener,
                                  final boolean isCancelable) {
        alertTwoButton(title, message, positiveText, onPositiveListener, null, null, isCancelable);
    }

    /**
     * 显示两个按钮的
     */
    public void alertTwoButton(final String title,
                               final CharSequence message,
                               final String positiveText,
                               final OnPositiveListener onPositiveListener,
                               final String negativeText,
                               final OnNegativeListener onNegativeListener,
                               final boolean isCancelable) {
        alert(title, message, positiveText, onPositiveListener, negativeText, onNegativeListener, null, null, isCancelable);
    }

    /**
     * 显示Neutral按钮的
     */
    public void alertNeutralButton(final String title,
                                   final CharSequence message,
                                   final String neutralText,
                                   final OnNeutralListener onNeutralListener,
                                   final boolean isCancelable) {
        alert(title, message, null, null, null, null, neutralText, onNeutralListener, isCancelable);
    }

    /**
     * 显示输入框dialog
     *
     * @param hint
     * @param positiveListener
     * @param negativeListener
     */
    public void alertEditTextMD(final String hint,
                                final PositiveListenerText positiveListener,
                                final NegativeListenerText negativeListener) {
        alertEditTextMD(null, hint, null, "确定", positiveListener, "取消", negativeListener, 0, 0, false, true);
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
     * @param isCancelable     点击外部是否消失
     * @param isFocusable      editText是否获取焦点并弹出键盘
     *                         <p>
     *                         输入类型为普通文本
     *                         editText.setInputType(InputType.TYPE_CLASS_TEXT);
     *                         <p>
     *                         //输入类型为数字文本
     *                         editText.setInputType(InputType.TYPE_CLASS_NUMBER);
     *                         <p>
     *                         //输入类型为电话号码
     *                         editText.setInputType(InputType.TYPE_CLASS_PHONE);
     *                         <p>
     *                         //输入类型为日期和时间
     *                         editText.setInputType(InputType.TYPE_CLASS_DATETIME);
     *                         /输入类型为自动完成文本类型
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
     *                         <p>
     *                         //输入类型为自动纠正文本类型
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
     *                         <p>
     *                         //输入类型为所有字符大写
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
     *                         <p>
     *                         //输入类型为每句的第一个字符大写
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
     *                         <p>
     *                         //输入类型为每个单词的第一个字母大写
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
     *                         <p>
     *                         //输入多行文本
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
     *                         <p>
     *                         //进行输入时，输入法无提示
     *                         editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
     *                         <p>
     *                         //输入一个短的，可能是非正式的消息，如即时消息或短信。
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
     *                         <p>
     *                         //输入长内容，可能是正式的消息内容，比如电子邮件的主体
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
     *                         <p>
     *                         //输入文本以过滤列表等内容
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
     *                         <p>
     *                         //输入一个电子邮件地址
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
     *                         <p>
     *                         //输入电子邮件主题行
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
     *                         <p>
     *                         //输入一个密码
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
     *                         <p>
     *                         //输入老式的普通文本
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
     *                         <p>
     *                         //输入人名
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
     *                         <p>
     *                         //输入邮寄地址
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
     *                         <p>
     *                         //输入语音发音输入文本，如联系人拼音名称字段
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
     *                         <p>
     *                         //输入URI
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
     *                         <p>
     *                         //输入对用户可见的密码
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
     *                         <p>
     *                         //输入网页表单中的文本
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
     *                         <p>
     *                         //输入网页表单中的邮件地址
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
     *                         <p>
     *                         //输入网页表单中的密码
     *                         editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
     */
    public void alertEditTextMD(final String title, final String hint, final String info,
                                final String positiveText,
                                final PositiveListenerText positiveListener,
                                final String negativeText,
                                final NegativeListenerText negativeListener,
                                final int maxLength,
                                final int inputType,
                                final boolean isCancelable, final boolean isFocusable) {
        dismissDialog();
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
                        builder.setCancelable(isCancelable);
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
                        initWidth(mActivity, mAlertDialog);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }

                }
            });
        }
    }

    /**
     * 列表dialog
     *
     * @param title
     * @param items
     * @param onItemListener
     * @param isCancelable
     */
    public void alertList(final String title, final String[] items, final OnItemListener onItemListener, final boolean isCancelable) {
        dismissDialog();
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppCompatAlertDialogStyle);
                    //标题
                    if (!TextUtils.isEmpty(title))
                        builder.setTitle(title);
                    //设置列表
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onItemListener.item(which);
                        }
                    });
                    mAlertDialog = builder.create();
                    mAlertDialog.show();

                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancelable);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                        initWidth(mActivity, mAlertDialog);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }
                }
            });
        }
    }


    int yourChoice = -1;

    /**
     * 单选列表
     */
    public void alertSingleChoice(final String title, final int checkedItem, final CharSequence[] items, final OnItemListener onItemListener, final boolean isCancelable) {
        dismissDialog();
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppCompatAlertDialogStyle);
                    //标题
                    if (!TextUtils.isEmpty(title))
                        builder.setTitle(title);

                    //设置列表
                    yourChoice = -1;
                    builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yourChoice = which;
                        }
                    });

                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (yourChoice != -1) {
                                        onItemListener.item(yourChoice);
                                    }
                                }
                            });

                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancelable);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                        initWidth(mActivity, mAlertDialog);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }
                }
            });
        }
    }


    /**
     * 多选列表
     */
    public void alertMultiChoice(final String title, final CharSequence[] items, final boolean[] initChoiceSets, final OnItemChecked onItemListener, final boolean isCancelable) {
        dismissDialog();
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppCompatAlertDialogStyle);
                    //标题
                    if (!TextUtils.isEmpty(title))
                        builder.setTitle(title);
                    //设置列表
                    builder.setMultiChoiceItems(items, initChoiceSets, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                initChoiceSets[which]=isChecked;
                        }
                    });

                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        onItemListener.item(initChoiceSets);
                                }
                            });

                    try {
                        //点击外部是否消失
                        builder.setCancelable(isCancelable);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                        initWidth(mActivity, mAlertDialog);
                    } catch (ParseException e) {
                        mAlertDialog = null;
                    }
                }
            });
        }
    }


    /**
     * 等待Dialog具有屏蔽其他控件的交互能力
     *
     */
    public static ProgressDialog getWaitingDialog(final Activity mActivity, final String title, final String info, final boolean isCancelable) {

        if (mActivity != null && !mActivity.isFinishing()) {

            ProgressDialog waitingDialog = new ProgressDialog(mActivity);
            //标题
            if (!TextUtils.isEmpty(title))
                waitingDialog.setTitle(title);

            //内容
            if (!TextUtils.isEmpty(info))
                waitingDialog.setMessage(info);

            //是否采用进度模糊模式
            waitingDialog.setIndeterminate(true);

            //是否可以取消
            waitingDialog.setCancelable(isCancelable);

            return waitingDialog;
        }

        return null;
    }

    public static ProgressDialog getProgressDialog(final Activity mActivity, final String title, final int max, final boolean isCancelable) {

        if (mActivity != null && !mActivity.isFinishing()) {

            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            //标题
            if (!TextUtils.isEmpty(title))
                progressDialog.setTitle(title);

            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(max);

            //是否可以取消
            progressDialog.setCancelable(isCancelable);

            return progressDialog;
        }

        return null;
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


    public interface OnPositiveListener {
        void positive();
    }

    public interface OnNegativeListener {
        void negative();
    }

    public interface OnNeutralListener {
        void neutral();
    }

    public interface OnItemListener {
        void item(int index);
    }

    public interface OnItemChecked {
        void item(boolean[] choiceSets);
    }

    public interface PositiveListenerText {
        void positive(String text);
    }

    public interface NegativeListenerText {
        void negative(String text);

    }

    /**
     * 防止dialog多次弹出
     */
    public void dismissDialog() {
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
     * 设置dialog宽度，需要在show之后调用
     *
     * @param activity
     * @param dialog
     */
    public static void initWidth(Activity activity, AlertDialog dialog) {
        initWidth(activity, dialog, 0.8f);// 宽度设置为屏幕的0.8
    }

    public static void initWidth(Activity activity, AlertDialog dialog, float scale) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (LjyScreenUtils.getScreenWidth(activity) * scale); //设置宽度
        p.dimAmount = (1f - scale) / 2f;
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
