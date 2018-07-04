package com.cai.pullrefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import com.cai.pullrefresh.swipemenulistview.OnMenuItemClickListener;
import com.cai.pullrefresh.swipemenulistview.OnSwipeListener;
import com.cai.pullrefresh.swipemenulistview.SwipeMenuLayout;


/**
 * Created by davy on 16/12/13.
 */

public class PtrRecyclerView extends RecyclerView {
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    private int MAX_Y = 5;
    private int MAX_X = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchState;
    private int mTouchPosition;
    private boolean swipeEnable;
    private SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;

    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;
    //
    private BasePtrAdapter mBasePtrAdapter;
    private LayoutManager mLayoutManager;
    //
    private boolean isScrollNow;

    public PtrRecyclerView(Context context) {
        this(context, null);
    }

    public PtrRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
        swipeEnable = true;
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != 0) {
                    isScrollNow = true;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof BasePtrAdapter)) {
            throw new IllegalArgumentException("Adapter 必须要继承 BasePtrAdapter");
        }
        if (!(adapter instanceof BasePtrSwipeAdapter)) {
            swipeEnable = false;
        }
        super.setAdapter(adapter);
        mBasePtrAdapter = (BasePtrAdapter) adapter;
        if (mLayoutManager != null)
            mBasePtrAdapter.setLayoutManager(mLayoutManager);
    }

    public BasePtrAdapter getAdapter() {
        return mBasePtrAdapter;
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager mGridLayoutManager = (GridLayoutManager) layout;
            int mOrientation = mGridLayoutManager.getOrientation();
            if (mOrientation != LinearLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("invalid orientation must VERTICAL");
            }
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mBasePtrAdapter == null) {
                        return mGridLayoutManager.getSpanCount();
                    } else {
                        return mBasePtrAdapter.getItemViewType(position) == 0 ? mGridLayoutManager.getSpanCount() : 1;
                    }
                }
            });
            //
        } else if (layout instanceof LinearLayoutManager) {
            LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) layout;
            int mOrientation = mLinearLayoutManager.getOrientation();
            if (mOrientation != LinearLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("invalid orientation must VERTICAL");
            }
            //
        } else if (layout instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layout;
            int mOrientation = mStaggeredGridLayoutManager.getOrientation();
            if (mOrientation != StaggeredGridLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("invalid orientation must VERTICAL");
            }
            //
        } else
            throw new IllegalArgumentException("只支持LinearLayoutManager 和 GridLayoutManager");
        //
        mLayoutManager = layout;
        if (mBasePtrAdapter != null) {
            mBasePtrAdapter.setLayoutManager(mLayoutManager);
        }
    }

    boolean isMoveing;
    long touchTime;
    boolean isLongClick;
    boolean isClick;
    boolean isSwipeOpen;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN && (mTouchView == null || !swipeEnable))
            return super.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchTime = System.currentTimeMillis();
                isLongClick = false;
                isClick = false;
                isScrollNow = false;
                isMoveing = false;
                isSwipeOpen = false;
                //
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;

                mTouchPosition = getChildLayoutPosition(findChildViewUnder(ev.getX(), ev.getY()));
                if (mTouchPosition == oldPos && mTouchView != null && mTouchView.isOpen()) {
                    isSwipeOpen = true;
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;
                }

                View view = getChildAt(mTouchPosition - getFirstVisibleItem());

                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    return super.onTouchEvent(ev);
                }
                //
                if (view instanceof SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                }
                //
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isMoveing && mTouchView != null)
                            mTouchView.setContentViewPressed(true);
                    }
                }, 80);
                //
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mTouchView != null && !isClick && !isScrollNow && !isMoveing) {
                            isLongClick = true;
                            mTouchView.onLongClick();
                        }
                    }
                }, 500);

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    //getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        isMoveing = true;
                        if (mTouchView != null) {
                            mTouchView.setContentViewPressed(false);
                        }
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        isMoveing = true;
                        if (mTouchView != null) {
                            mTouchView.setContentViewPressed(false);
                        }
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchView != null) {
                    long time = System.currentTimeMillis() - touchTime;
                    if (time < 80 && !isScrollNow && !isSwipeOpen) {
                        mTouchView.setContentViewPressed(true);

                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mTouchView != null)
                                    mTouchView.setContentViewPressed(false);
                            }
                        }, 5);

                    } else {
                        mTouchView.setContentViewPressed(false);
                    }
                }
                if (!isScrollNow && !isMoveing && !isSwipeOpen && !isLongClick && mTouchView != null) {
                    mTouchView.onClick();
                    isClick = true;
                }
                isScrollNow = false;
                isMoveing = true;
                isLongClick = false;
                //
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                        if (!mTouchView.isOpen()) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void smoothOpenMenu(int position, int duration) {
        if (position >= getFirstVisibleItem()
                && position <= getFindLastVisibleItemPosition()) {
            View view = getChildAt(position - getFirstVisibleItem());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    return;
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothOpenMenu(duration);
            }
        }
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisibleItem()
                && position <= getFindLastVisibleItemPosition()) {
            View view = getChildAt(position - getFirstVisibleItem());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    return;
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothOpenMenu();
            }
        }
    }

    public void smoothCloseMenu(int position, int duration) {
        if (position >= getFirstVisibleItem()
                && position <= getFindLastVisibleItemPosition()) {
            View view = getChildAt(position - getFirstVisibleItem());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && !mTouchView.isOpen()) {
                    return;
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothCloseMenu(duration);
            }
        }
    }

    public void smoothCloseMenu(int position) {
        if (position >= getFirstVisibleItem()
                && position <= getFindLastVisibleItemPosition()) {
            View view = getChildAt(position - getFirstVisibleItem());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && !mTouchView.isOpen()) {
                    return;
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothCloseMenu();
            }
        }
    }


    public void setSwpieEnable(boolean enable) {
        swipeEnable = enable;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        if (mBasePtrAdapter instanceof BasePtrSwipeAdapter) {
            ((BasePtrSwipeAdapter) mBasePtrAdapter).setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }


    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }


    /**
     * 获取可见区域第一个item的position
     *
     * @return
     */
    public int getFirstVisibleItem() {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) mLayoutManager;
            int[] into = new int[mStaggeredGridLayoutManager.getSpanCount()];
            mStaggeredGridLayoutManager.findFirstVisibleItemPositions(into);
            return min(into);

        } else if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager mGridLayoutManager = (GridLayoutManager) mLayoutManager;
            return mGridLayoutManager.findFirstVisibleItemPosition();
        } else {
            LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mLayoutManager;
            return mLinearLayoutManager.findFirstVisibleItemPosition();
        }
    }

    /**
     * 获取最后一个可见的item
     *
     * @return
     */
    public int getFindLastVisibleItemPosition() {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) mLayoutManager;
            int into[] = new int[mStaggeredGridLayoutManager.getSpanCount()];
            mStaggeredGridLayoutManager.findLastVisibleItemPositions(into);
            return max(into);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager mGridLayoutManager = (GridLayoutManager) mLayoutManager;
            return mGridLayoutManager.findLastVisibleItemPosition();
        } else {
            LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mLayoutManager;
            return mLinearLayoutManager.findLastVisibleItemPosition();
        }
    }


    /**
     * 获取可见的item个数
     *
     * @return
     */
    public int getVisibleItemCount() {
        return getFindLastVisibleItemPosition() - getFirstVisibleItem();
    }

    /**
     * 获取总共的数据个数
     *
     * @return
     */
    public int getTotalItemCount() {
        return getAdapter() == null ? 0 : getAdapter().getCount();
    }

    //获取最小值
    private int min(int[] into) {
        if (into == null)
            return -1;
        int size = into.length;
        int min = -1;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                min = into[i];
            } else if (min > into[i]) {
                min = into[i];
            }

        }
        return min;
    }

    //获取最大值
    private int max(int[] into) {
        if (into == null)
            return -1;
        int size = into.length;
        int max = -1;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                max = into[i];
            } else if (max < into[i]) {
                max = into[i];
            }
        }
        return max;
    }
}
