package com.cai.pullrefresh;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by davy on 16/11/28.
 */

public class PtrListView extends ListView implements AbsListView.OnScrollListener {
    private List<OnScrollListener> onScrollListeners;

    public PtrListView(Context context) {
        super(context);
        init();
    }

    public PtrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PtrListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PtrListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        onScrollListeners = new ArrayList<>();
        //
        setDivider(null);
        setFooterDividersEnabled(false);
        setCacheColorHint(0);
        setSelector(new ColorDrawable(0));
        //
        setOnScrollListener(this);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l.hashCode() == hashCode())
            super.setOnScrollListener(l);
        else {
            onScrollListeners.add(l);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        for (OnScrollListener l : onScrollListeners) {
            l.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (OnScrollListener l : onScrollListeners) {
            l.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
