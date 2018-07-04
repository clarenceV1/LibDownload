package com.cai.pullrefresh;

import android.view.View;
import android.view.ViewGroup;

import com.cai.pullrefresh.swipemenulistview.OnMenuItemClickListener;
import com.cai.pullrefresh.swipemenulistview.SwipeMenuLayout;
import com.cai.pullrefresh.swipemenulistview.SwipeMenu;
import com.cai.pullrefresh.swipemenulistview.SwipeMenuView;

/**
 * Created by davy on 17/6/5.
 */

public abstract class BasePtrSwipeAdapter<T, H extends BasePtrViewHold> extends BasePtrAdapter<T, H> implements SwipeMenuView.OnSwipeItemClickListener {

    private OnMenuItemClickListener onMenuItemClickListener;

    protected void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }


    @Override
    protected void onPtrBindViewHolder(H holder, T data, int position) {
        if (holder.itemView instanceof SwipeMenuLayout) {
            ((SwipeMenuLayout) holder.itemView).setPosition(position);
        }

    }

    @Override
    protected BasePtrViewHold onPtrCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getItemView(parent);
        SwipeMenu menu = new SwipeMenu(parent.getContext());
        menu.setViewType(viewType);
        createMenu(menu);
        SwipeMenuView menuView = new SwipeMenuView(menu);
        menuView.setOnSwipeItemClickListener(this);
        PtrRecyclerView listView = (PtrRecyclerView) parent;
        SwipeMenuLayout layout = new SwipeMenuLayout(itemView, menuView, listView.getCloseInterpolator(), listView.getOpenInterpolator());
        return createSwipeViewHolder(layout, viewType);
    }


    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
        }
    }


    /**
     * 获取contentView
     *
     * @return
     */
    public abstract View getItemView(ViewGroup parent);

    /**
     * 创建侧滑菜单
     *
     * @param menu
     */
    public abstract void createMenu(SwipeMenu menu);


    public abstract BasePtrViewHold createSwipeViewHolder(View itemParent, int viewType);

}
