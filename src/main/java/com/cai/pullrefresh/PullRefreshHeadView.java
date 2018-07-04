package com.cai.pullrefresh;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.cai.pullrefresh.lib.PtrFrameLayout;


/**
 * 刷新头部view
 * Created by davy on 16/11/28.
 */

public class PullRefreshHeadView extends PullRefreshLayout {
    private View rootView;
    private TextView textTV;//下拉刷新文字的view
    private TextView timeTV;//时间view
    //private ImageView iconIV;//
   // private StarView starView;
    //private AnimationDrawable iconAnimation;

    public PullRefreshHeadView(Context context) {
        this(context, null);
    }

    public PullRefreshHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    //初始化
    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_meetyou_pullrefresh_head, this);
        textTV = (TextView) findViewById(R.id.pullrefresh_text);
        timeTV = (TextView) findViewById(R.id.pullrefresh_time);
        //iconIV = (ImageView) findViewById(R.id.pullrefresh_icon);
        //FrameLayout star_frame = (FrameLayout) findViewById(R.id.pullfresh_star_frame);
        //starView= new StarView(getContext());
        //star_frame.addView(starView);
        //
        //iconIV.setImageResource(R.drawable.refresh_icon_list_image);
        //iconAnimation = (AnimationDrawable) iconIV.getDrawable();
        //iconAnimation.setOneShot(false);
        //
        setLastUpdateTimeKey(getClass().getName());
    }

    //重置UI
    private void reSetUI() {
        textTV.setText(R.string.pull_to_refresh_pull_label);
        //iconAnimation.stop();
        //iconAnimation.selectDrawable(0);
        //starView.pause();
    }

    //刷新中
    private void onRefreshBegin() {
        //iconAnimation.stop();
        //iconAnimation.selectDrawable(0);
        //iconAnimation.start();
        //starView.resume();
        textTV.setText(R.string.pull_to_refresh_refreshing_label);
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        reSetUI();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        timeTV.setText("上次刷新时间：" + getLastRefreshTime());
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        onRefreshBegin();
    }

    @Override
    protected void upCancelRefresh() {
        textTV.setText(R.string.pull_to_refresh_pull_label);
    }

    @Override
    protected void upStartRefresh() {
        textTV.setText(R.string.pull_to_refresh_release_label);
    }

    @Override
    protected void onRefreshComplete(PtrFrameLayout frame) {//完成刷新
       // starView.recycleStar();
    }
}
