package com.cai.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cai.pullrefresh.lib.PtrDefaultHandler;
import com.cai.pullrefresh.lib.PtrFrameLayout;
import com.cai.pullrefresh.lib.PtrHandler;
import com.cai.pullrefresh.swipemenulistview.BasePtrFrameLayout;


/**
 * 下拉刷新基类
 * Created by davy on 16/11/28.
 */

public class BaseListPtrFrameLayout extends BasePtrFrameLayout implements PtrHandler {

    protected int inLastItemLoading = 3;//当处于倒数第N个item的时候自动加载更多
    protected boolean isRefreshing;//是否在刷新中
    protected boolean isLoadMoreing;//是否正在加载更多
    protected boolean isNoMore;//是否是没有更多数据
    private boolean isCloseRefresh;//是否关闭刷新
    protected boolean isCloseLoadMore;//是否关闭加载更多的功能

    public BaseListPtrFrameLayout(Context context) {
        super(context);
    }

    public BaseListPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseListPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (isCloseRefresh || isLoadMoreing) {
            return false;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    /**
     * 这是保存时间的key值，一般用于单独页面设置，直接传页面对象进来
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        mPullRefreshHeadView.setLastUpdateTimeRelateObject(object);
    }


    /**
     * 设置处于倒数对应的item自动加载更多
     *
     * @param position
     */
    public void setInLastItemLoading(int position) {
        if (position <= 1) {
            position = 1;
        }
        inLastItemLoading = position;
    }

    /**
     * 设置是否关闭加载更多的功能
     *
     * @param isCloseLoadMore
     */
    public void setCloseLoadMore(boolean isCloseLoadMore) {
        this.isCloseLoadMore = isCloseLoadMore;
    }

    /**
     * 设置是否关闭下拉刷新功能
     */
    public void setCloseRefresh(boolean isCloseRefresh) {
        this.isCloseRefresh = isCloseRefresh;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        isRefreshing = false;
       super.onRefreshBegin(frame);
    }

    /**
     * 完成加载更多
     *
     * @param isCanLoadMore 是否可以加载更多
     */
    public void loadMoreComplete(boolean isCanLoadMore) {
        isLoadMoreing = false;
        isNoMore = !isCanLoadMore;
    }

    //开始加载更多
    protected void startLoadMore() {
        if (isNoMore || isLoadMoreing || isRefreshing) {
            return;
        }
        if (isCloseLoadMore) {
            updateListViewFooter(ListFooterUtil.ListViewFooterState.HIDE, "");
            return;
        }
        isLoadMoreing = true;
        updateListViewFooter(ListFooterUtil.ListViewFooterState.LOADING, "");
        if (mOnPullLoadListener != null) {
            mOnPullLoadListener.onLoadMore();
        }
    }

    /**
     * 刷新或者加载更多完成
     *
     * @param isCanLoadMore 加载更多使用，用于判断是否还可以加载更多
     */
    public void refreshOrLoadMoreComplete(boolean isCanLoadMore) {
        if (isRefreshing) {
            refreshComplete();
        } else if (isLoadMoreing) {
            loadMoreComplete(isCanLoadMore);
        } else {
            refreshComplete();
            loadMoreComplete(isCanLoadMore);
        }

    }

    /**
     * 更新底部item内容
     */
    protected void updateListViewFooter(ListFooterUtil.ListViewFooterState state, String text) {
    }

}
