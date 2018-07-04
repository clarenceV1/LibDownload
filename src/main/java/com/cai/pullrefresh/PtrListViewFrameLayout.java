package com.cai.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.cai.pullrefresh.lib.PtrUIHandlerHook;


/**
 * 简单的封装listview的view
 * Created by davy on 16/11/28.
 */

public class PtrListViewFrameLayout extends BaseListPtrFrameLayout implements AbsListView.OnScrollListener {
    private PtrListView mListView;

    private View footerView;
    //
    private int scrollState;//listview滚动状态

    public PtrListViewFrameLayout(Context context) {
        super(context);
        initView();
    }

    public PtrListViewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrListViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mListView = new PtrListView(getContext());
        addView(mListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //
        footerView = ListFooterUtil.getInstance().getListViewFooter(LayoutInflater.from(getContext()));
        //
        initListView();
    }

    //初始化listview部分逻辑
    private void initListView() {
        mListView.setOnScrollListener(this);
        mListView.addFooterView(footerView);
        ListFooterUtil.getInstance().hideListViewFooter(footerView);
        isLoadMoreing = false;
        setRefreshCompleteHook(new PtrUIHandlerHook() {
            @Override
            public void run() {//hook刷新完成
                isLoadMoreing = false;
                resume();//释放

            }
        });
    }

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
    protected void updateListViewFooter(ListFooterUtil.ListViewFooterState state, String text) {
        super.updateListViewFooter(state, text);
        ListFooterUtil.getInstance().updateListViewFooter(footerView, state, text);
    }

    /**
     * @return
     */
    public ListView getListView() {
        return mListView;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollState == SCROLL_STATE_IDLE) {//停止不滚动的时候
            return;
        }
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        if (visibleItemCount == totalItemCount && totalItemCount < 10) {//显示的item个数等于总数据
            return;
        }

        if (totalItemCount - lastVisibleItem <= inLastItemLoading) {
            startLoadMore();
        }
    }

}
