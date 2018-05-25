package com.ljy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljy.util.LjyChineseToPinyinUtil;
import com.ljy.util.LjyLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJY on 2018/5/8 11:01
 */

public abstract class LjyBaseFilterAdapter<T extends LjyBaseFilterAdapter.BaseFilterBean> extends RecyclerView.Adapter<LjyBaseFilterAdapter.LjyViewHolder> implements Filterable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private Context mContext;
    public List<T> list;
    public List<T> listCopy;
    private int mItemLayoutId;
    private View headerView;

    public LjyBaseFilterAdapter(Context context) {
        this.mContext = context;
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = new ArrayList<>();
        this.listCopy = new ArrayList<>();

    }

    public LjyBaseFilterAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = list;
        listCopy = new ArrayList<>();
        listCopy.addAll(list);
    }


    public LjyBaseFilterAdapter(Context context, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.list = new ArrayList<>();
        this.listCopy = new ArrayList<>();
    }

    public LjyBaseFilterAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.list = list;
        listCopy = new ArrayList<>();
        listCopy.addAll(list);
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setNewList(List<T> newList) {
        this.list = newList == null ? new ArrayList<T>() : newList;
        listCopy = new ArrayList<>();
        listCopy.addAll(newList);
        notifyDataSetChanged();
    }

    public void addList(List<T> addList) {
        this.list.addAll(addList);
        this.listCopy.addAll(addList);
        notifyItemRangeInserted(this.list.size() - addList.size(), addList.size());
    }

    @Override
    public int getItemCount() {
        return list.size() + (headerView == null ? 0 : 1);

    }

    /**
     * 添加HeaderView实际就是多种item布局
     */

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(LjyViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER)
            return;
        convert(holder, list.get(position - (headerView == null ? 0 : 1)));
    }

    @Override
    public LjyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new LjyViewHolder(headerView);
        return LjyViewHolder.get(mContext, parent, mItemLayoutId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return getItemViewType(i) == TYPE_HEADER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(LjyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParamsStaggeredGrid = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            layoutParamsStaggeredGrid.setFullSpan(holder.getLayoutPosition() == 0);
        }
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

        public static LjyViewHolder get(Context context, ViewGroup parent, int layoutId) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (results.values == null) {
                    list.clear();
                    list.addAll(listCopy);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.values = listCopy;
                    results.count = listCopy.size();
                } else {
                    String prefixString = constraint.toString().toLowerCase();
                    final int count = list.size();
                    ArrayList<T> newValues = new ArrayList();
                    for (int i = 0; i < count; i++) {
                        T value = list.get(i);
                        String filterKey = value.getFilterKey();
                        if (filterKey != null && (filterKey.toLowerCase().contains(prefixString)
                                || LjyChineseToPinyinUtil.getInstance().getPinyin(filterKey).toLowerCase().contains(prefixString))) {
                            newValues.add(value);
                        }

                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((List<T>) results.values);
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    if (constraint.length() > 0) {
                        notifyDataSetChanged();
                        return;
                    }
                    setNewList(listCopy);
                }
            }
        };
    }

    public static class BaseFilterBean {
        String filterKey;

        public String getFilterKey() {
            return filterKey;
        }

        public void setFilterKey(String filterKey) {
            this.filterKey = filterKey;
        }
    }
}
