package com.ljy.ljyutils.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyColorUtil;
import com.ljy.view.LjySwipeRefreshView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefreshListViewActivity extends BaseActivity {

    @BindView(R.id.swipeRefreshView)
    LjySwipeRefreshView swipeRefreshView;
    @BindView(R.id.listView)
    ListView listView;
    private ArrayList<Map<String, Object>> listems;
    private MyAdapter mAdapter;
    private int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list_view);
        ButterKnife.bind(mActivity);
        initView();
    }

    private void initView() {
        swipeRefreshView.setRefreshProgressColor(R.color.theme_red);
        swipeRefreshView.setLoadMoreProgressColor(R.color.theme_red);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        swipeRefreshView.setOnLoadMoreListener(new LjySwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });

        listems = new ArrayList<>();
        listems.addAll(initData(start, start += 4));
        mAdapter = new MyAdapter(mContext, listems);
        swipeRefreshView.initListViewHeader(listView);
        listView.setAdapter(mAdapter);
    }

    private Collection<? extends Map<String, Object>> initData(int start, int end) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            Map<String, Object> listem = new HashMap<>();
            listem.put("head", R.mipmap.ic_launcher);
            listem.put("name", "name_" + i);
            list.add(listem);
        }
        return list;
    }

    private void loadMoreData() {
        swipeRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshView.setLoadMoreSuccess();
                listems.addAll(initData(start + 1, start += 5));
                mAdapter.notifyDataSetChanged();
            }
        }, 100 * 8);
    }

    private void refreshData() {
        swipeRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshView.setRefreshing(false);
                start = 1;
                listems.clear();
                listems.addAll(initData(start, start += 4));
                mAdapter.notifyDataSetChanged();
            }
        }, 100 * 8);
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        public List<Map<String, Object>> mData;
        private Map<String, Object> data;

        public MyAdapter(Context context, List<Map<String, Object>> dataList) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = dataList;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.layout_item_list, null);
                holder.imgIcon = convertView.findViewById(R.id.imgIcon);
                holder.textTitle = convertView.findViewById(R.id.textName);
                holder.textInfo = convertView.findViewById(R.id.textInfo);
                holder.itemRoot = convertView.findViewById(R.id.itemRoot);
                holder.itemRoot.setBackgroundColor(LjyColorUtil.getInstance().randomColor());
                convertView.setTag(holder);
            }

            data = mData.get(position);
            holder.imgIcon.setImageResource((Integer) data.get("head"));
            holder.textTitle.setText((CharSequence) data.get("name"));
            holder.textInfo.setText((position + 1) + "/" + getCount());

            return convertView;
        }

        public final class ViewHolder {
            private TextView textTitle;
            private TextView textInfo;
            private ImageView imgIcon;
            public LinearLayout itemRoot;
        }

    }
}
