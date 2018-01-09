package com.ljy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * Created by Mr.LJY on 2016/11/8.
 *
 * 可添加header/footer的recyclerView，配合LjySwipeRefreshView使用
 */
public class LjyRecyclerView extends RecyclerView {

    public ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();
    public ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    public static final int BASE_HEADER_VIEW_TYPE = -1 << 10;
    public static final int BASE_FOOTER_VIEW_TYPE = -1 << 11;

    public LjyRecyclerView(Context context) {
        this(context,null);
    }

    public LjyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LjyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addFooterView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        info.viewType = BASE_FOOTER_VIEW_TYPE + mFooterViewInfos.size();
        mFooterViewInfos.add(info);

        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public View getFooterView(int position) {
        if (mFooterViewInfos.isEmpty()) {
            throw new IllegalStateException("you must add a FooterView before!");
        }
        return mFooterViewInfos.get(position).view;
    }

    public void addHeaderView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        info.viewType = BASE_FOOTER_VIEW_TYPE + mHeaderViewInfos.size();
        mHeaderViewInfos.add(info);

        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof WrapperRecyclerViewAdapter))
            adapter  = new WrapperRecyclerViewAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        super.setAdapter(adapter);

    }

    public  int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }
    public  int getHeaderViewsCount() {
        return mHeaderViewInfos.size();
    }

    public class FixedViewInfo {
        public View view;
        public int viewType;
    }

    public class WrapperRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ArrayList<LjyRecyclerView.FixedViewInfo> mHeaderViewInfos;
        public ArrayList<LjyRecyclerView.FixedViewInfo> mFooterViewInfos;
        public RecyclerView.Adapter mAdapter;
        public ArrayList<LjyRecyclerView.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();
        private boolean isStaggered;
        private boolean isFooter;

        public WrapperRecyclerViewAdapter(ArrayList<LjyRecyclerView.FixedViewInfo> headerViewInfos,
                                          ArrayList<LjyRecyclerView.FixedViewInfo> footerViewInfos,
                                          RecyclerView.Adapter adapter) {
            mAdapter = adapter;
            if (headerViewInfos == null) {
                mHeaderViewInfos = EMPTY_INFO_LIST;
            } else {
                mHeaderViewInfos = headerViewInfos;
            }
            if (footerViewInfos == null) {
                mFooterViewInfos = EMPTY_INFO_LIST;
            } else {
                mFooterViewInfos = footerViewInfos;
            }
        }

        public int getHeadersCount() {
            return mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return mFooterViewInfos.size();
        }

        public boolean isEmpty() {
            return mAdapter == null;
        }

        public boolean removeHeader(View view) {
            for (int i = 0; i < mHeaderViewInfos.size(); i++) {
                if (mHeaderViewInfos.get(i).view == view) {
                    mHeaderViewInfos.remove(i);
                    return true;
                }
            }
            return false;
        }

        public boolean removeFooter(View view) {
            for (int i = 0; i < mFooterViewInfos.size(); i++) {
                if (mFooterViewInfos.get(i).view == view) {
                    mFooterViewInfos.remove(i);
                    return true;
                }
            }
            return false;
        }

        public void adjustSpanSize(RecyclerView recyclerView) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int numHeaders = getHeadersCount();
                        int adjPosition = position - numHeaders;
                        if (position < numHeaders || adjPosition >= mAdapter.getItemCount())
                            return manager.getSpanCount();
                        return 1;
                    }
                });
            }

            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                isStaggered = true;
            }
        }

        public void setVisibility(View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (view.getVisibility() == View.GONE) {
                params.width = 0;
                params.height = 0;
            } else {
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            view.setLayoutParams(params);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType >= LjyRecyclerView.BASE_HEADER_VIEW_TYPE && viewType < LjyRecyclerView.BASE_HEADER_VIEW_TYPE + getHeadersCount()) {
                View view = mHeaderViewInfos.get(viewType - LjyRecyclerView.BASE_HEADER_VIEW_TYPE).view;
                isFooter = false;
//            setVisibility(view);
                return viewHolder(view);
            } else if (viewType >= LjyRecyclerView.BASE_FOOTER_VIEW_TYPE && viewType < LjyRecyclerView.BASE_FOOTER_VIEW_TYPE + getFootersCount()) {
                View view = mFooterViewInfos.get(viewType - LjyRecyclerView.BASE_FOOTER_VIEW_TYPE).view;
                isFooter = true;
                setVisibility(view);
                return viewHolder(view);
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return;
            }
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mAdapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }

        }

        @Override
        public long getItemId(int position) {
            int numHeaders = getHeadersCount();
            if (mAdapter != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                int adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public int getItemViewType(int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return mHeaderViewInfos.get(position).viewType;
            }
            int adjPosition = position - numHeaders;
            int adapterPosition = 0;
            if (mAdapter != null) {
                adapterPosition = mAdapter.getItemCount();
                if (adjPosition < adapterPosition) {
                    return mAdapter.getItemViewType(adjPosition);
                }
            }

            return mFooterViewInfos.get(position - adapterPosition - getHeadersCount()).viewType;
        }

        @Override
        public int getItemCount() {
            if (mAdapter != null) {
                return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        private RecyclerView.ViewHolder viewHolder(final View itemView) {

            if (isStaggered) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
                        0);
//            if (!isFooter)
//                params.setMargins(0, -130, 0, 0);
                params.setFullSpan(true);
                itemView.setLayoutParams(params);
            }
            return new RecyclerView.ViewHolder(itemView) {
            };
        }
    }

}
