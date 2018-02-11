package com.ljy.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljy.lib.R;


/**
 * Created by Mr.LJY on 2018/1/2.
 *
 * 辩论（红蓝对决）View
 */

public class LjyArgueProgressView  extends LinearLayout {

        private ProgressBar progress_argue_view;
        private TextView text_argue_title_yes_view;
        private TextView text_argue_title_no_view;
        private TextView text_argue_num_yes_view;
        private TextView text_argue_num_no_view;
        private LinearLayout btn_argue_yes_view;
        private LinearLayout btn_argue_no_view;
        private TextView text_argue_content_no_view;
        private TextView text_argue_content_yes_view;
        private ImageView imgView_argue_yes;
        private ImageView imgView_argue_no;

        public LjyArgueProgressView(Context context, AttributeSet attrs) {
            super(context, attrs);
            LayoutInflater.from(context).inflate(R.layout.layout_ljy_argue_progress_view, this, true);
            initView();
        }

        public void initView() {
            progress_argue_view = (ProgressBar) findViewById(R.id.progress_argue);
            text_argue_title_yes_view = (TextView) findViewById(R.id.text_argue_title_yes);
            text_argue_title_no_view = (TextView) findViewById(R.id.text_argue_title_no);
            text_argue_num_yes_view = (TextView) findViewById(R.id.text_argue_num_yes);
            text_argue_num_no_view = (TextView) findViewById(R.id.text_argue_num_no);
            text_argue_content_yes_view = (TextView) findViewById(R.id.text_argue_content_yes);
            text_argue_content_no_view = (TextView) findViewById(R.id.text_argue_content_no);
            btn_argue_yes_view = (LinearLayout) findViewById(R.id.btn_argue_yes);
            btn_argue_no_view = (LinearLayout) findViewById(R.id.btn_argue_no);
            imgView_argue_yes = (ImageView) findViewById(R.id.imgView_argue_yes);
            imgView_argue_no = (ImageView) findViewById(R.id.imgView_argue_no);
        }

        public void makeBtnGray() {
            btn_argue_yes_view.setBackgroundResource(R.drawable.ljy_argue_btnbg_gray);
            btn_argue_no_view.setBackgroundResource(R.drawable.ljy_argue_btnbg_gray);
            imgView_argue_yes.setImageResource(R.drawable.bbs_argue_yes_gray);
            imgView_argue_no.setImageResource(R.drawable.bbs_argue_no_gray);
            setOnClickListener(new OnClickListener() {
                @Override
                public boolean listenerYes() {
                    return false;
                }

                @Override
                public boolean listenerNo() {
                    return false;
                }
            });
        }

        public void setUI(int numYes, int numNo, String infoYes, String infoNo) {
            int percentYes = numYes == 0 && numNo == 0 ? 0 : (int) (numYes / (float) (numYes + numNo) * 100);
            int percentNo = numYes == 0 && numNo == 0 ? 0 : (100 - percentYes);
            text_argue_title_yes_view.setText(percentYes + "% 正方");
            text_argue_title_no_view.setText(percentNo + "% 反方");
            progress_argue_view.setProgress(numYes == 0 && numNo == 0 ? 0 : 100);
            progress_argue_view.setSecondaryProgress(percentYes);
//        progress_argue_view.setProgress(percentYes);
            text_argue_num_yes_view.setText(numYes + "人");
            text_argue_num_no_view.setText(numNo + "人");
            if (!TextUtils.isEmpty(infoYes)) {
                text_argue_content_yes_view.setText(Html
                        .fromHtml( infoYes));
            }
            if (!TextUtils.isEmpty(infoNo)) {
                text_argue_content_no_view.setText(Html
                        .fromHtml( infoNo));
            }
        }

        public void setOnClickListener(final OnClickListener onClickListener) {
            btn_argue_yes_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickable(false);
                    if (onClickListener != null)
                        onClickListener.listenerYes();
                }
            });
            btn_argue_no_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickable(false);
                    if (onClickListener != null)
                        onClickListener.listenerNo();
                }
            });

        }

        public void btnClickable(boolean clickable) {
            btn_argue_no_view.setClickable(clickable);
            btn_argue_yes_view.setClickable(clickable);
        }


        public interface OnClickListener {
            boolean listenerYes();

            boolean listenerNo();
        }
    }
