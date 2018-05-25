package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.ljy.adapter.LjyBaseAdapter;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.SwipeCardBean;
import com.ljy.util.LjyGlideUtil;
import com.ljy.view.LjyRecyclerView;
import com.ljy.view.LjySwipeRefreshView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefreshRecyclerViewActivity extends BaseActivity {

    @BindView(R.id.swipeRefreshView)
    LjySwipeRefreshView mSwipeRefreshView;
    @BindView(R.id.recyclerView)
    LjyRecyclerView mRecyclerView;
    private LjyBaseAdapter<SwipeCardBean> mAdapter;
    private int pageCount=0,pageSize=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_recycler_view);
        ButterKnife.bind(mActivity);
        initView();
    }

    private void initView() {

        mSwipeRefreshView.setRefreshProgressColor(R.color.thin_red);
        mSwipeRefreshView.setLoadMoreProgressColor(R.color.thin_red);
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mSwipeRefreshView.setOnLoadMoreListener(new LjySwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<SwipeCardBean> listData=SwipeCardBean.initData(pageCount++*pageSize,pageCount*pageSize);
        mRecyclerView.setAdapter(mAdapter=new LjyBaseAdapter<SwipeCardBean>(mContext,listData , R.layout.layout_item_list) {
            @Override
            public void convert(LjyViewHolder holder, SwipeCardBean item) {
                holder.setBackgroundColor(R.id.itemRoot, item.getCardColor());
                holder.setText(R.id.textName, item.getName());
                holder.setText(R.id.textInfo, item.getPosition() + "/" + mAdapter.getItemCount());
                LjyGlideUtil.loadImg(mContext,item.getUrl(),(ImageView) holder.getView(R.id.imgIcon));
            }
        });
    }

    private void loadMoreData() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addList(SwipeCardBean.initData(pageCount++*pageSize,pageCount*pageSize));
                mSwipeRefreshView.setLoadMoreSuccess();
            }
        }, 100 * 8);
    }

    private void refreshData() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageCount=0;
                mAdapter.setNewList(SwipeCardBean.initData(pageCount++*pageSize,pageCount*pageSize));
                mSwipeRefreshView.setRefreshing(false);
            }
        }, 100 * 8);
    }
}
