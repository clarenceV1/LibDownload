package com.cai.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.cai.pullrefresh.lib.PtrDefaultHandler;
import com.cai.pullrefresh.lib.PtrFrameLayout;
import com.cai.pullrefresh.swipemenulistview.BasePtrFrameLayout;

/**
 * Created by davy on 2018/3/23.
 */

public class PtrScrollViewFrameLayout extends BasePtrFrameLayout {
    public PtrScrollViewFrameLayout(Context context) {
        this(context, null);
    }

    public PtrScrollViewFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrScrollViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (content instanceof ScrollView) {
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }
}
