package com.cai.pullrefresh.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cai.pullrefresh.PullRefreshHeadView;
import com.cai.pullrefresh.lib.PtrFrameLayout;
import com.cai.pullrefresh.lib.PtrHandler;

public class BasePtrFrameLayout extends PtrFrameLayout implements PtrHandler {
    protected PullRefreshHeadView mPullRefreshHeadView;
    protected OnPullLoadListener mOnPullLoadListener;

    public BasePtrFrameLayout(Context context) {
        this(context, null);
    }

    public BasePtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        baseInit();
    }

    private void baseInit() {
        mPullRefreshHeadView = new PullRefreshHeadView(getContext());
        setHeaderView(mPullRefreshHeadView);
        addPtrUIHandler(mPullRefreshHeadView);
        //
        setPtrHandler(this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (mOnPullLoadListener != null) {
            mOnPullLoadListener.onRefresh(frame);
        }
    }

    /**
     * 设置刷新监听
     *
     * @param listener
     */
    public void setOnPullLoadListener(OnPullLoadListener listener) {
        mOnPullLoadListener = listener;
    }

    public interface OnPullLoadListener {
        /**
         * 刷新
         */
        void onRefresh(PtrFrameLayout frame);

        /**
         * 加载更多
         */
        void onLoadMore();
    }
}
