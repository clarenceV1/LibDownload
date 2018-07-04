package com.cai.pullrefresh;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cai.pullrefresh.swipemenulistview.SwipeMenuLayout;


/**
 * RecyclerView ViewHolder 基类
 * Created by davy on 16/12/13.
 */
public class BaseViewHold extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private OnRecyclerViewItemClickListener listener;

    public BaseViewHold(View itemView, OnRecyclerViewItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        if (itemView instanceof SwipeMenuLayout) {
            ((SwipeMenuLayout) itemView).setPtrOnClickListener(this);
            ((SwipeMenuLayout) itemView).setPtrOnLongClickListener(this);
        } else {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
    }

    /**
     * 获取布局中的View
     *
     * @param viewId view的Id
     * @param <T>    View的类型
     * @return view
     */
    protected <T extends View> T getView(@IdRes int viewId) {
        return (T) (itemView.findViewById(viewId));
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onItemClick(v, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null)
            listener.onItemLongClick(v, getAdapterPosition());
        return true;
    }

    /**
     * item点击事件
     */
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}
