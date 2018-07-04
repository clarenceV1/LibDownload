package com.cai.pullrefresh.lib;


import com.cai.pullrefresh.lib.indicator.PtrIndicator;

/**
 *
 */
public interface PtrUIHandler {

    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    public void onUIReset(PtrFrameLayout frame);

    /**
     * prepare for loading 下拉的时候出发，主要用于改变UI
     *
     * @param frame
     */
    public void onUIRefreshPrepare(PtrFrameLayout frame);

    /**
     * perform refreshing UI 刷新中状态
     */
    public void onUIRefreshBegin(PtrFrameLayout frame);

    /**
     * perform UI after refresh  刷新完成
     */
    public void onUIRefreshComplete(PtrFrameLayout frame);

    /**
     *UI 移动回调,主要用于判断是否达到触发释放刷新
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
