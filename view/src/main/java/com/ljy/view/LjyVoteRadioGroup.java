package com.ljy.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ljy.ljyview.R;
import com.ljy.util.LjySystemUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/1/2.
 *
 * 单选投票View
 */

public class LjyVoteRadioGroup extends RadioGroup {
    private Context mContext;
    // CheckBox 列表
    private List<RadioButton> mRadioButtons;
    // 每一个CheckBox 显示的内容
    private String mSelectedRadioContent;
    private List<LinearLayout> mLinearLayout_son2s;
    private List<ProgressBar> mProgressBars;
    private List<TextView> mTextViews;
    private SubmitButtonListener submitListener;
    public static int VOTE_BTN_SIZE = 60;
    public static int PADDING_ITEM=40;
    public static int PADDING_LEFT=33;
    public static int HEIGHT_PROGRESS=12;

    public LjyVoteRadioGroup(Context context) {
        this(context, null);
    }


    public LjyVoteRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        this.setOrientation(VERTICAL);
        mRadioButtons = new ArrayList<>();
        mProgressBars = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mLinearLayout_son2s=new ArrayList<>();
        mSelectedRadioContent = "";
        VOTE_BTN_SIZE= LjySystemUtil.dp2px(context,16);
        PADDING_ITEM= LjySystemUtil.dp2px(context,4);
        PADDING_LEFT= LjySystemUtil.dp2px(context,12);
        HEIGHT_PROGRESS= LjySystemUtil.dp2px(context,8);
    }


    /**
     * 获取已选中的项
     *
     * @return
     */
    public String getSelectedRadioContent() {
        return mSelectedRadioContent;
    }

    public void setCheckRadios(List<VoteBean> radioContents) {
        if (radioContents == null )
            return;

        int radioBoxCount = radioContents.size();
        mRadioButtons.clear();
        mLinearLayout_son2s.clear();
        mProgressBars.clear();
        mTextViews.clear();
        this.removeAllViews();
        for (int i = 0; i < radioBoxCount; i++) {
            VoteBean polloption = radioContents.get(i);
            initRadioButton(this, polloption, i);
        }
    }

    public interface SubmitButtonListener {
        void onListener(boolean isCanUse);
    }

    public void setSubmitListener(SubmitButtonListener listener) {
        this.submitListener = listener;
    }



    private void initRadioButton(RadioGroup linearLayout, VoteBean polloption,
                                 int index) {
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setTextSize(15.0f);
        radioButton.setTextColor(Color.parseColor("#88000000"));
        radioButton.setOnCheckedChangeListener(listener);
        radioButton.setTag(polloption.getVoteId());
        radioButton.setMaxLines(2);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                Field field = radioButton.getClass().getSuperclass().getDeclaredField("mButtonDrawable");
                field.setAccessible(true);
                field.set(radioButton, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            radioButton.setButtonDrawable(null);
        }
        radioButton.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_radio_notselect);
        drawable.setBounds(0, 0, VOTE_BTN_SIZE, VOTE_BTN_SIZE);
        radioButton.setCompoundDrawables(drawable, null, null, null);
        mRadioButtons.add(radioButton);
        TextView textView = new TextView(mContext);
        textView.setTextSize(14);
        textView.setTextColor(0xff333333);
        textView.setText(++index + ". " + polloption.getPolloption());
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textParams.weight = 1.0f;
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                HEIGHT_PROGRESS);
        progressParams.gravity = Gravity.CENTER_VERTICAL;
        progressParams.weight = 1.0f;
        LinearLayout.LayoutParams radioButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioButtonParams.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout linearLayout_son = new LinearLayout(mContext);
        linearLayout_son.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout_son.setPadding(0,PADDING_ITEM,0,0);
        LinearLayout.LayoutParams lyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout_son.addView(textView, textParams);
        radioButton.setPadding(PADDING_LEFT, 0, 0, 0);
        linearLayout_son.addView(radioButton, radioButtonParams);
        LinearLayout linearLayout_son2 = new LinearLayout(mContext);
        linearLayout_son2.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.bbs_progressbar_color));
        TextView textView_percent = new TextView(mContext);
        textView_percent.setPadding(PADDING_LEFT, 0, 0, 0);
        textView_percent.setTextSize(14);
        textView_percent.setTextColor(0xff999999);
        mProgressBars.add(progressBar);
        linearLayout_son2.addView(progressBar, progressParams);
        linearLayout_son2.setVisibility(GONE);
        mTextViews.add(textView_percent);
        linearLayout_son2.addView(textView_percent, radioButtonParams);
        mLinearLayout_son2s.add(linearLayout_son2);
        linearLayout.addView(linearLayout_son, lyParams);
        linearLayout.addView(linearLayout_son2, lyParams);
    }
    public void changeUI(List<VoteBean> list) {
        double sum = 0.0;
        for (VoteBean polloption : list) {
            sum += polloption.getVotes();
        }
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < mRadioButtons.size(); i++) {
            int currentVote = list.get(i).getVotes();
            mProgressBars.get(i).setMax((int) sum);
            mProgressBars.get(i).setProgress(currentVote);
            mTextViews.get(i).setText(currentVote + "(" + format.format(currentVote / sum * 100) + "%)");
            mLinearLayout_son2s.get(i).setVisibility(VISIBLE);
            mRadioButtons.get(i).setVisibility(INVISIBLE);
        }
    }
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) {// 添加已选中的复选框
                mSelectedRadioContent = buttonView.getTag().toString();
                for (RadioButton rb : mRadioButtons) {
                    if (!rb.getTag().toString().equals(mSelectedRadioContent))
                        rb.setChecked(false);
                }
            }
            for (RadioButton radioButton : mRadioButtons) {
                Drawable drawable;
                if (!radioButton.isChecked()) {
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_radio_notselect);
                    radioButton.setClickable(true);
                } else {
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.bbs_radio_select);
                }
                drawable.setBounds(0, 0, VOTE_BTN_SIZE, VOTE_BTN_SIZE);
                radioButton.setCompoundDrawables(drawable, null, null, null);
            }
            boolean hasSelect = false;
            for (RadioButton radioButton : mRadioButtons) {
                if (radioButton.isChecked()) {
                    hasSelect = true;
                    break;
                }
            }
            if (submitListener != null)
                submitListener.onListener(hasSelect);

        }
    };

    public static class VoteBean {

        private int voteId;
        private int pollid;
        private int votes;
        private String polloption;
        private String voternames;

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }

        public int getPollid() {
            return pollid;
        }

        public void setPollid(int pollid) {
            this.pollid = pollid;
        }

        public int getVotes() {
            return votes;
        }

        public void setVotes(int votes) {
            this.votes = votes;
        }

        public String getPolloption() {
            return polloption;
        }

        public void setPolloption(String polloption) {
            this.polloption = polloption;
        }

        public String getVoternames() {
            return voternames;
        }

        public void setVoternames(String voternames) {
            this.voternames = voternames;
        }

    }

}