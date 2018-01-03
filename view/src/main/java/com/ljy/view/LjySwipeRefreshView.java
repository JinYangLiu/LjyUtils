package com.ljy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ljy.ljyview.R;


/**
 * Created by Mr.LJY on 2017/8/18.
 */

public class LjySwipeRefreshView extends SwipeRefreshLayout {

    private final int mScaledTouchSlop;
    private final LinearLayout mListViewFooterParent;
    private final LjySwipeFooter mListViewFooter;
    private final LjySwipeFooter mRecyclerViewFooter;
    private ListView mListView;
    private OnLoadMoreListener mListener;
    private int mTouchSlop;
    private float mPrevX;
    /**
     * 正在加载状态
     */
    private boolean isLoading;
    private LjyRecyclerView mRecyclerView;
    private int mItemCount = 5;
    private OnScrollListener mOnScrollListener;
    private boolean mIsRefreshing;

    public LjySwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mRecyclerViewFooter = new LjySwipeFooter(context);
        mListViewFooter = new LjySwipeFooter(context);
        mListViewFooterParent = new LinearLayout(context);
        mListViewFooterParent.setOrientation(LinearLayout.VERTICAL);
        mListViewFooterParent.addView(mListViewFooter);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 5;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null || mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断是不是ListView/RecyclerView
                for (int i = 0; i < getChildCount(); i++) {
                    if (getChildAt(i) instanceof ListView) {
                        // 创建ListView对象
                        mListView = (ListView) getChildAt(i);
                        // 设置ListView的滑动监听
                        setListViewOnScroll(mOnScrollListener);
                        break;
                    } else if (getChildAt(i) instanceof LjyRecyclerView) {
                        // 创建ListView对象
                        mRecyclerView = (LjyRecyclerView) getChildAt(i);
                        // 设置RecyclerView的滑动监听
                        setRecyclerViewOnScroll();
                        mRecyclerView.setOnTouchListener(
                                new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if (mIsRefreshing) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                        );

                        break;
                    }
                }
            }
        }
    }

    /**
     * 此方法解决4.x版本不显示footer问题，需要在listView的setAdapter之前调用
     *
     * @param listView
     */
    public void initListViewHeader(ListView listView) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(1, 1);
        View header = new View(getContext());
        header.setLayoutParams(lp);
        header.setVisibility(View.GONE);
        listView.addHeaderView(header);
    }

    public void initRecycleViewHeader(LjyRecyclerView recyclerView) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(1, 1);
        View header = new View(getContext());
        header.setLayoutParams(lp);
        header.setVisibility(View.GONE);
        recyclerView.addHeaderView(header);
    }

    /**
     * 解决ViewPager左右滑动和SwipeToRefresh下拉刷新冲突的方案
     *
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY, mUpY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                mUpY = ev.getY();
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                break;
        }
        boolean res;
        try {
            res = super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }
        return res;
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;

        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {

            if (mItemCount > 0) {
                if (mListView.getAdapter().getCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false;
                } else {
                    condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
            }

        } else if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            if (mItemCount > 0) {
                if (mRecyclerView.getAdapter().getItemCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false;
                } else {
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        condition2 = linearManager.findLastVisibleItemPosition() + mRecyclerView.getFooterViewsCount() >= (linearManager.getItemCount() - 1);
                        condition2 |= mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset() >= mRecyclerView.computeVerticalScrollRange();
                    }
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    condition2 = linearManager.findLastVisibleItemPosition() + mRecyclerView.getFooterViewsCount() >= (linearManager.getItemCount() - 1);
                    condition2 |= mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset() >= mRecyclerView.computeVerticalScrollRange();
                }
            }
        }


        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        return condition1 && condition2 && condition3;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }


    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
        }

    }

    public void setLoadMoreSuccess() {
        setLoading(false);
    }

    public void setLoadMoreProgressColor(int... colors) {
        mRecyclerViewFooter.setProgressDrawable(colors);
        mListViewFooter.setProgressDrawable(colors);
    }

    public void setRefreshProgressColor(int... colors) {
        setColorSchemeResources(colors);
    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    private void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if (isLoading) {
            // 显示布局
            if (mListView != null && mListView.getFooterViewsCount() < 1)
                mListView.addFooterView(mListViewFooterParent, null, false);
            if (mRecyclerView != null && mRecyclerView.getFooterViewsCount() < 1)
                mRecyclerView.addFooterView(mRecyclerViewFooter);
            if (mRecyclerView != null) {
                ViewGroup.LayoutParams params = mRecyclerView.getFooterView(0).getLayoutParams();
                params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                mRecyclerView.getFooterView(0).setLayoutParams(params);
                mRecyclerView.getFooterView(0).setVisibility(View.VISIBLE);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            if (mListView != null) {
                mListViewFooter.setVisibility(VISIBLE);
            }
        } else {
            // 隐藏布局
            if (mRecyclerView != null) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mRecyclerView.getFooterView(0).getLayoutParams();
                params.width = 0;
                params.height = 0;
                mRecyclerView.getFooterView(0).setLayoutParams(params);
                mRecyclerView.getFooterView(0).setVisibility(View.GONE);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            if (mListView != null) {
                mListViewFooter.setVisibility(GONE);
            }
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }

    public LjySwipeFooter getFooter() {
        return mRecyclerViewFooter;
    }


    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll(final OnScrollListener listener) {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                if (listener != null)
                    listener.onScrollStateChanged(view, scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listener != null)
                    listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    /**
     * 设置滚动监听
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                      int totalItemCount);
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }


    @Override
    public void setRefreshing(boolean refreshing) {
        mIsRefreshing = refreshing;
        super.setRefreshing(refreshing);
    }

    public static class LjySwipeFooter extends LinearLayout {

        private ProgressBar progressBar;
        private LjySwipeFooter mFooter;
        private LayoutInflater mLayoutInflater;

        public LjySwipeFooter(Context context) {
            super(context);
            mLayoutInflater = LayoutInflater.from(context);
            mFooter = (LjySwipeFooter) mLayoutInflater.inflate(R.layout.layout_swpie_footer, this, true);
            progressBar = mFooter.findViewById(R.id.loading_progress);
            setVisibility(VISIBLE);
        }


        public LjySwipeFooter(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public void setProgressDrawable(int... colorResIds) {
            CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
            final Context context = getContext();
            int[] colorRes = new int[colorResIds.length];
            for (int i = 0; i < colorResIds.length; i++) {
                colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
            }
            drawable.setColorSchemeColors(colorRes);
            drawable.setStyle(CircularProgressDrawable.DEFAULT);
            progressBar.setIndeterminateDrawable(drawable);
        }
    }

}
