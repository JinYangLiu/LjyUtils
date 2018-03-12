package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.adapter.GroupListViewAdapter;
import com.ljy.ljyutils.bean.SortEntity;
import com.ljy.view.LjySideIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SideIndexBarActivity extends AppCompatActivity {
    private ListView mLvFriends;
    private List<SortEntity> mFriends;
    private GroupListViewAdapter mAdapter;
    private SortEntity mSortEntity;
    private LjySideIndexBar mSideBar;
    private TextView mTvDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_index_bar);
        initView();
        initEvent();
    }

    private void initView(){
        mLvFriends =  findViewById(R.id.lv_main);
        mSideBar =  findViewById(R.id.sidebar);
        mTvDialog =  findViewById(R.id.tv_dialog);
        mSideBar.setTvDialog(mTvDialog);
        mSortEntity = new SortEntity();

    }

    private void initEvent(){
        mFriends = fillData(getResources().getStringArray(R.array.title));
        Collections.sort(mFriends, mSortEntity);    //对list排序
        mAdapter = new GroupListViewAdapter(mFriends, this);
        //接口的回调
        mSideBar.setOnTouchLetterChangeListener(new LjySideIndexBar.OnTouchLetterChangeListener() {
            @Override
            public void letterChange(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLvFriends.setSelection(position);
                }
            }
        });
        mLvFriends.setAdapter(mAdapter);
    }

    //填充数据
    private List<SortEntity> fillData(String[] data) {
        List<SortEntity> entities = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            SortEntity sortEntity = new SortEntity();
            sortEntity.setName(data[i]);
            sortEntity.setSortLetters(data[i].substring(0, 1).toUpperCase());
            entities.add(sortEntity);
        }
        return entities;
    }
}
