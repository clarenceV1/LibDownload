package com.cai.pullrefresh;

import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 适配器基类
 * Created by davy on 16/12/13.
 */
public abstract class BaseAdapter<T, H extends BasePtrViewHold> extends RecyclerView.Adapter<H> implements BaseViewHold.OnRecyclerViewItemClickListener {

    private List<BaseViewHold.OnRecyclerViewItemClickListener> mOnRecycleViewItemClickListeners;

    protected List<T> datas;

    public BaseAdapter() {
        datas = new ArrayList<>();

    }

    /**
     * 设置数据
     *
     * @param newdatas
     */
    public void setDatas(List<T> newdatas) {
        if (newdatas != null)
            datas.clear();
        addDatas(newdatas);
    }

    /**
     * 增加数据
     *
     * @param newdatas
     */
    public void addDatas(List<T> newdatas) {
        addDatas(-1, newdatas);
    }

    /**
     * 增加数据
     *
     * @param position 指定位置开始增加
     * @param newdatas
     */
    public void addDatas(int position, List<T> newdatas) {
        if (newdatas != null) {
            if (datas.size() == 0) {//首次添加数据
                datas.addAll(newdatas);
                notifyDataSetChanged();
            } else {
                int positionStart = (position < 0 || position > datas.size()) ? datas.size() : position;
                datas.addAll(positionStart, newdatas);
                notifyItemRangeChanged(positionStart, newdatas.size());
            }
        }
    }

    /**
     * 增加单条数据
     *
     * @param newData
     */
    public void addData(T newData) {
        addData(datas.size(), newData);
    }

    /**
     * 在指定位置增加单条数据
     *
     * @param position
     * @param newData
     */
    public void addData(int position, T newData) {
        if (position >= 0 && position <= datas.size()) {
            datas.add(position, newData);
            notifyItemInserted(position);
        }
    }

    /**
     * 删除指定位置的数据
     *
     * @param position
     */
    public void removeData(int position) {
        if (position >= 0 && position < datas.size()) {
            datas.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 获取指定位置的数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        if (position >= 0 && position < datas.size())
            return datas.get(position);
        else
            return null;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> getDatas() {
        return datas;
    }

    @Override
    public void onItemClick(View v, int position) {
        dispatchOnItemClick(v, position);
    }

    @Override
    public void onItemLongClick(View v, int position) {
        dispatchOnItemLongClick(v, position);
    }

    /**
     * 添加item点击事件监听器
     *
     * @param listener
     */
    public void addOnRecycleViewItemClickListener(BaseViewHold.OnRecyclerViewItemClickListener listener) {
        if (mOnRecycleViewItemClickListeners == null)
            mOnRecycleViewItemClickListeners = new ArrayList<>();
        mOnRecycleViewItemClickListeners.add(listener);
    }

    /**
     * 移除指定的item点击事件监听
     *
     * @param listener
     */
    public void removeOnRecycleViewItemClickListener(BaseViewHold.OnRecyclerViewItemClickListener listener) {
        if (mOnRecycleViewItemClickListeners != null)
            mOnRecycleViewItemClickListeners.remove(listener);
    }

    /**
     * 情况item点击事件监听
     */
    public void clearOnRecycleViewItemClickListener() {
        if (mOnRecycleViewItemClickListeners != null)
            mOnRecycleViewItemClickListeners.clear();
    }

    //分发点击事件
    protected void dispatchOnItemClick(View v, int position) {
        if (mOnRecycleViewItemClickListeners != null) {
            for (BaseViewHold.OnRecyclerViewItemClickListener listener : mOnRecycleViewItemClickListeners) {
                listener.onItemClick(v, position);
            }
        }
    }

    //分发长按事件
    protected void dispatchOnItemLongClick(View v, int position) {
        if (mOnRecycleViewItemClickListeners != null) {
            for (BaseViewHold.OnRecyclerViewItemClickListener listener : mOnRecycleViewItemClickListeners) {
                listener.onItemLongClick(v, position);
            }
        }
    }
}
