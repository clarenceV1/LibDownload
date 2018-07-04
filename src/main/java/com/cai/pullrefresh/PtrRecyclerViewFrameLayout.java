package com.cai.pullrefresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;


import com.cai.pullrefresh.lib.PtrUIHandlerHook;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * RecyclerView 下拉刷新
 * Created by davy on 16/12/10.
 */

public class PtrRecyclerViewFrameLayout extends BaseListPtrFrameLayout {
    private PtrRecyclerView mRecyclerView;
    private int scrollState;//recyclerView滚动状态

    public PtrRecyclerViewFrameLayout(Context context) {
        super(context);
        initView();
    }

    public PtrRecyclerViewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrRecyclerViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mRecyclerView = new PtrRecyclerView(getContext());
        addView(mRecyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initRecyclerView();
    }

    /**
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 完成加载更多
     *
     * @param isCanLoadMore 是否可以加载更多
     */
    @Override
    public void loadMoreComplete(boolean isCanLoadMore) {
        super.loadMoreComplete(isCanLoadMore);
        if (isCanLoadMore) {
            updateListViewFooter(ListFooterUtil.ListViewFooterState.HIDE, "");
        } else {
            if (isCloseLoadMore) {
                updateListViewFooter(ListFooterUtil.ListViewFooterState.HIDE, "");
            }else {
                updateListViewFooter(ListFooterUtil.ListViewFooterState.COMPLETE, "");
            }
        }
    }

    @Override
    protected void updateListViewFooter(final ListFooterUtil.ListViewFooterState state, final String text) {
        super.updateListViewFooter(state, text);
        if (getAdapter() == null) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                getAdapter().setFooterViewState(state, text);
            }
        });

    }

    private void initRecyclerView() {
        isLoadMoreing = false;
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        setRefreshCompleteHook(new PtrUIHandlerHook() {
            @Override
            public void run() {//hook刷新完成
                isLoadMoreing = false;
                resume();//释放

            }
        });
    }


    //recyclerview滚动监听
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            scrollState = newState;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //
            if (scrollState == SCROLL_STATE_IDLE) {//停止不滚动的时候
                return;
            }
            //
            int lastVisibleItem = mRecyclerView.getFindLastVisibleItemPosition();
            int visibleItemCount = mRecyclerView.getVisibleItemCount();
            int totalItemCount = mRecyclerView.getTotalItemCount();
            //GridLayoutManager 转成行数
            if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager mGridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                int spanCount = mGridLayoutManager.getSpanCount();
                lastVisibleItem = lastVisibleItem / spanCount + (haveRemainder(lastVisibleItem, spanCount) ? 1 : 0);
                visibleItemCount = visibleItemCount / spanCount + (haveRemainder(visibleItemCount, spanCount) ? 1 : 0);
                totalItemCount = totalItemCount / spanCount + (haveRemainder(totalItemCount, spanCount) ? 1 : 0);

            }
            if (visibleItemCount == totalItemCount && totalItemCount < 10) {//显示的item个数等于总数据
                return;
            }

            if (totalItemCount - lastVisibleItem <= inLastItemLoading) {
                startLoadMore();
            }
        }

    };

    //是否有余数
    private boolean haveRemainder(int divisor, int dividend) {
        return divisor % dividend > 0 ? true : false;
    }


    private BasePtrAdapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

}
