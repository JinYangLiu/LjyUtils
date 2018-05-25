package com.ljy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/1/15.
 */

public abstract class LjyBaseAdapter<T> extends RecyclerView.Adapter<LjyBaseAdapter.LjyViewHolder> {
    private Context mContext;
    public List<T> list;
    private int mItemLayoutId;

    public LjyBaseAdapter(Context context) {
        this.mContext = context;
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = new ArrayList<T>();

    }

    public LjyBaseAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = list;

    }

    public LjyBaseAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.list = list;

    }

    public LjyBaseAdapter(Context context, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.list = new ArrayList<T>();

    }

    public void setitemLayoutId(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setNewList(List<T> newList) {
        this.list=newList == null ? new ArrayList<T>() : newList;
        notifyDataSetChanged();
    }

    public void addList(List<T> addList) {
        this.list.addAll(addList);
        notifyItemRangeInserted(this.list.size()-addList.size(),addList.size());
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    boolean hasHeader = false;
    boolean hasFooter = false;
    View headerView;
    View footerView;

    public void setHeaderView(View headerView) {
        hasHeader = true;
        this.headerView = headerView;
    }

    public void setFooterView(View footerView) {
        hasFooter = true;
        this.footerView = footerView;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    @Override
    public void onBindViewHolder(LjyViewHolder holder, int position) {
        if (hasHeader && position == 0) {
            return;
        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
            return;
        } else
            convert(holder, (T) list.get(position));
    }

    @Override
    public LjyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        if (hasHeader && position == 0) {
            return new LjyViewHolder(headerView);
        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
            return new LjyViewHolder(footerView);
        } else
            return LjyViewHolder.get(mContext, parent, mItemLayoutId, position);

    }

    //这里定义抽象方法，我们在匿名内部类实现的时候实现此方法来调用控件
    public abstract void convert(LjyViewHolder holder, T item);

    public static class LjyViewHolder extends RecyclerView.ViewHolder {

        private View mConvertView;
        private SparseArray<View> mViews;

        public LjyViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            this.mViews = new SparseArray<>();
        }

        public static LjyViewHolder get(Context context, ViewGroup parent, int layoutId, int position) {
            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new LjyViewHolder(view);
        }

        /**
         * 通过控件的Id获取对于的控件，如果没有则加入views
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {

            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 为TextView设置字符�?
         *
         * @param viewId
         * @param text
         * @return
         */
        public RecyclerView.ViewHolder setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 设置背景颜色
         *
         * @param viewId
         * @param color
         * @return
         */
        public RecyclerView.ViewHolder setBackgroundColor(int viewId, int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @param listener
         * @return
         */
        public RecyclerView.ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param drawableId
         * @return
         */
        public RecyclerView.ViewHolder setImageResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);

            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param bm
         * @return
         */
        public RecyclerView.ViewHolder setImageBitmap(int viewId, Bitmap bm) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bm);
            return this;
        }

        public View getConvertView() {
            return mConvertView;
        }
    }
}
