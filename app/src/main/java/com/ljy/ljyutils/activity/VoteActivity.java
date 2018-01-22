package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyVoteCheckBox;
import com.ljy.view.LjyVoteRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteActivity extends BaseActivity {

    @BindView(R.id.button_submit)
    Button btn_submit;
    @BindView(R.id.myCheckBoxs)
    LjyVoteCheckBox voteCheckBoxs;
    @BindView(R.id.myRadioButtons)
    LjyVoteRadioGroup voeRadioGroup;
    @BindView(R.id.textView_voteType)
    TextView textVoteType;
    @BindView(R.id.textView_voteType2)
    TextView textVoteType2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(mActivity);
        initVoteView();
    }

    private void initVoteView() {
        List<LjyVoteRadioGroup.VoteBean> list_item = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LjyVoteRadioGroup.VoteBean bean = new LjyVoteRadioGroup.VoteBean();
            bean.setVoteId(i);
            bean.setPolloption("Polloption_" + i);
            bean.setVoternames("Voternames_" + i);
            bean.setVotes(i * 10);
            bean.setPollid(i * 100);
            list_item.add(bean);
        }
        voteCheckBoxs.setCheckBoxs(list_item);
        int maxNum = 2;
        voteCheckBoxs.setMaxNum(maxNum);
        textVoteType.setText(String.format("多选投票（最多可选%1$s项）", maxNum));
        voeRadioGroup.setCheckRadios(list_item);
        textVoteType2.setText(String.format("单选投票（最多可选%1$s项）", 1));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = voteCheckBoxs.getSelectedBoxContents();
                StringBuffer sbuffer = new StringBuffer();
                sbuffer.append("CheckBox_");
                for (int i = 0; i < list.size(); i++) {
                    sbuffer.append(list.get(i));
                    sbuffer.append(",");
                }
                sbuffer.append("RadioGroup_");
                sbuffer.append(voeRadioGroup.getSelectedRadioContent());
                String select = sbuffer.toString();
                LjyToastUtil.toast(mContext, "voteId:" + select);
            }
        });
    }
}
