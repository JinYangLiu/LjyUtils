package com.ljy.ljyutils.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyVoteCheckBox;
import com.ljy.view.LjyVoteRadioGroup;

import java.util.ArrayList;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    private Button btn_submit;
    private LjyVoteCheckBox voteCheckBoxs;
    private LjyVoteRadioGroup voeRadioGroup;
    private TextView textVoteType;
    private TextView textVoteType2;
    private Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        initVoteView();
    }

    private void initVoteView() {
        //投票贴
        btn_submit = findViewById(R.id.button_submit);
        voteCheckBoxs = findViewById(R.id.myCheckBoxs);
        voeRadioGroup =  findViewById(R.id.myRadioButtons);
        textVoteType =  findViewById(R.id.textView_voteType);
        textVoteType2 =  findViewById(R.id.textView_voteType2);
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
                LjyToastUtil.toast(mContext,"voteId:"+ select);
            }
        });
    }
}
