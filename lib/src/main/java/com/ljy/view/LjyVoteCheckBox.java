package com.ljy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljy.lib.R;
import com.ljy.util.LjySystemUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mr.LJY on 2018/1/2.
 *
 * 多选投票View
 */

public class LjyVoteCheckBox extends LinearLayout {

    private Context mContext;
    /**
     * CheckBox 列表
     */
    private List<LinearLayout> mLinearLayout_son2s;
    private List<CheckBox> mCheckBoxs;
    private List<TextView> mTextViews;
    private List<ProgressBar> mProgressBars;
    /**
     * 每一个CheckBox 显示的内容
     */
    private List<String> mSelectedBoxContents;
    private LjyVoteRadioGroup.SubmitButtonListener submitListener;
    private int maxNum;

    public LjyVoteCheckBox(Context context) {
        this(context, null);
    }

    public LjyVoteCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public LjyVoteCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.setOrientation(VERTICAL);
        mCheckBoxs = new ArrayList<>();
        mProgressBars = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mLinearLayout_son2s = new ArrayList<>();
        mSelectedBoxContents = new ArrayList<>();
        LjyVoteRadioGroup.VOTE_BTN_SIZE= LjySystemUtil.dp2px(context,16);
        LjyVoteRadioGroup.PADDING_ITEM= LjySystemUtil.dp2px(context,4);
        LjyVoteRadioGroup.PADDING_LEFT= LjySystemUtil.dp2px(context,12);
        LjyVoteRadioGroup.HEIGHT_PROGRESS= LjySystemUtil.dp2px(context,8);
    }

    /**
     * 获取已选中的项
     *
     * @return
     */
    public List<String> getSelectedBoxContents() {
        return mSelectedBoxContents;
    }

    public void changeUI(List<LjyVoteRadioGroup.VoteBean> list) {
        double sum = 0.0;
        for (LjyVoteRadioGroup.VoteBean polloption : list) {
            sum += polloption.getVotes();
        }
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < mCheckBoxs.size(); i++) {
            int currentVote = list.get(i).getVotes();
            mProgressBars.get(i).setMax((int) sum);
            mProgressBars.get(i).setProgress(currentVote);
            mTextViews.get(i).setText(currentVote + "(" + format.format(currentVote / sum * 100) + "%)");
            mCheckBoxs.get(i).setVisibility(INVISIBLE);
            mLinearLayout_son2s.get(i).setVisibility(VISIBLE);
        }
    }

    public void setSubmitListener(LjyVoteRadioGroup.SubmitButtonListener listener) {
        this.submitListener = listener;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setCheckBoxs(List<LjyVoteRadioGroup.VoteBean> checkBoxContents) {
        if (checkBoxContents == null )
            return;

        int checkBoxCount = checkBoxContents.size();
        mCheckBoxs.clear();
        mLinearLayout_son2s.clear();
        mProgressBars.clear();
        mTextViews.clear();
        this.removeAllViews();
        for (int i = 0; i < checkBoxCount; i++) {
            LjyVoteRadioGroup.VoteBean polloption = checkBoxContents.get(i);
            initCheckBox(this, polloption, i);
        }

    }

    private void initCheckBox(LinearLayout linearLayout, LjyVoteRadioGroup.VoteBean polloption,
                              int index) {
        CheckBox checkBox = new CheckBox(mContext);
        checkBox.setTextSize(15.0f);
        checkBox.setTextColor(Color.parseColor("#88000000"));
        checkBox.setOnCheckedChangeListener(listener);
        checkBox.setTag(polloption.getVoteId());
        checkBox.setMaxLines(2);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                Field field = checkBox.getClass().getSuperclass().getDeclaredField("mButtonDrawable");
                field.setAccessible(true);
                field.set(checkBox, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            checkBox.setButtonDrawable(null);
        }
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_check_notselect);
        drawable.setBounds(0, 0, LjyVoteRadioGroup.VOTE_BTN_SIZE, LjyVoteRadioGroup.VOTE_BTN_SIZE);
        checkBox.setCompoundDrawables(drawable, null, null, null);
        checkBox.setChecked(false);
        mCheckBoxs.add(checkBox);
        TextView textView = new TextView(mContext);
        textView.setTextSize(14);
        textView.setTextColor(0xff333333);
        textView.setText(++index + "." + polloption.getPolloption());
        LayoutParams textParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        LayoutParams checkBoxParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        checkBoxParams.gravity = Gravity.CENTER_VERTICAL;
        textParams.weight = 1.0f;
        LinearLayout linearLayout_son = new LinearLayout(mContext);
        linearLayout_son.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout_son.setPadding(0,LjyVoteRadioGroup.PADDING_ITEM,0,0);
        LayoutParams lyParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        linearLayout_son.addView(textView, textParams);
        checkBox.setPadding(LjyVoteRadioGroup.PADDING_LEFT, 0, 0, 0);
        linearLayout_son.addView(checkBox, checkBoxParams);
        LinearLayout linearLayout_son2 = new LinearLayout(mContext);
        linearLayout_son2.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams progressParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LjyVoteRadioGroup.HEIGHT_PROGRESS);
        progressParams.gravity = Gravity.CENTER_VERTICAL;
        progressParams.weight = 1.0f;
        ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.bbs_progressbar_color));
        TextView textView_percent = new TextView(mContext);
        textView_percent.setTextSize(14);
        textView_percent.setTextColor(0xff999999);
        textView_percent.setPadding(LjyVoteRadioGroup.PADDING_LEFT, 0, 0, 0);
        mProgressBars.add(progressBar);
        linearLayout_son2.addView(progressBar, progressParams);
        mTextViews.add(textView_percent);
        linearLayout_son2.addView(textView_percent, checkBoxParams);
        linearLayout_son2.setVisibility(GONE);
        mLinearLayout_son2s.add(linearLayout_son2);
        linearLayout.addView(linearLayout_son, lyParams);
        linearLayout.addView(linearLayout_son2, lyParams);
    }

    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            int currentNum = 0;
            for (CheckBox checkBox : mCheckBoxs) {
                if (checkBox.isChecked()) {
                    currentNum++;
                }
            }
            if (isChecked) {// 添加已选中的复选框
                if (currentNum <= maxNum) {
                    mSelectedBoxContents.add(buttonView.getTag().toString());
                } else {
                    buttonView.setChecked(false);
                }
            } else {
                mSelectedBoxContents.remove(buttonView.getTag().toString());
            }

            for (CheckBox checkBox : mCheckBoxs) {
                Drawable drawable;
                if (!checkBox.isChecked()) {
                    if (currentNum == maxNum) {
                        drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_check_select_unuse);
                        checkBox.setClickable(false);
                    } else {
                        drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_check_notselect);
                        checkBox.setClickable(true);
                    }
                } else {
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_check_select);
                }
                drawable.setBounds(0, 0, LjyVoteRadioGroup.VOTE_BTN_SIZE, LjyVoteRadioGroup.VOTE_BTN_SIZE);
                checkBox.setCompoundDrawables(drawable, null, null, null);
            }

            boolean hasSelect = false;
            for (CheckBox checkBox : mCheckBoxs) {
                if (checkBox.isChecked()) {
                    hasSelect = true;
                    break;
                }
            }
            if (submitListener != null)
                submitListener.onListener(hasSelect);
        }
    };

}
