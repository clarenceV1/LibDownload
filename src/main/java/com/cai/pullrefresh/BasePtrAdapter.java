package com.cai.pullrefresh;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ptr recyclerview 加载更多 适配器
 * Created by davy on 16/12/13.
 */

public abstract class BasePtrAdapter<T, H extends BasePtrViewHold> extends BaseAdapter<T, BasePtrViewHold> {

    private ListFooterUtil.ListViewFooterState state = ListFooterUtil.ListViewFooterState.HIDE;
    private String footerText;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 布局管理类赋值
     *
     * @param layoutManager
     */
    protected final void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }


    @Override
    public void onBindViewHolder(BasePtrViewHold holder, int position) {
        if (position >= datas.size()) {//在总数上偷偷加了1，所以最后一条是footerview
            ListFooterUtil.getInstance().updateListViewFooter(holder.itemView, state, footerText);
        } else
            onPtrBindViewHolder((H) holder, getData(position), position);
    }

    @Override
    public BasePtrViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View footView = ListFooterUtil.getInstance().getListViewFooter(LayoutInflater.from(parent.getContext()), parent, false);
            return new FootViewHold(footView, null);
        } else
            return onPtrCreateViewHolder(parent, viewType - 1);//因为footer需要占用一个样式，所以在getItemViewType偷偷的加上了1，所以此处需要减一
    }

    /**
     * 更新底部加载更多状态
     *
     * @param state
     */
    public void setFooterViewState(ListFooterUtil.ListViewFooterState state, String text) {
        this.state = state;
        footerText = text;
        notifyItemChanged(getCount());
    }

    /**
     * 注意！！注意！！注意！！ 此方法禁止重写！！！
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= datas.size())
            return 0;
        else
            return getPtrItemViewType(position) + 1;
    }

    public int getPtrItemViewType(int position) {
        return 0;
    }


    /**
     * 注意！！注意！！注意！！ 此方法禁止重写！！！
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    /**
     * 因为加载更多的需要，所以需使用getCount替换getItemCount，否则会出现无法加载更多的情况
     *
     * @return
     */
    public int getCount() {
        return datas.size();
    }

    private class FootViewHold extends BasePtrViewHold {


        public FootViewHold(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView, listener);
            if (mLayoutManager != null && mLayoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
                params.setFullSpan(true);
                itemView.setLayoutParams(params);
            }
        }
    }

    public View inflateItemView(ViewGroup parent, @LayoutRes int resource) {
       return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    }

    /**
     * @param holder
     * @param data
     * @param position
     */
    protected abstract void onPtrBindViewHolder(H holder, T data, int position);

    /**
     * @param parent
     * @param viewType
     */
    protected abstract BasePtrViewHold onPtrCreateViewHolder(ViewGroup parent, int viewType);
}
