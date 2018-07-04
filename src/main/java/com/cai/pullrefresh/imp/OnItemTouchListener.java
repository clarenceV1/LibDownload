package com.cai.pullrefresh.imp;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnItemTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        init(rv);
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        init(rv);
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void init(RecyclerView rv) {
        if (mGestureDetectorCompat == null) {
            mRecyclerView = rv;
            mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                    new MyGestureListener());
        }
    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            View child = getChildView(e);
            if (child != null) {
               // child.setClickable(true);
                child.onTouchEvent(e);
                Log.e("Test", "onDown hashCode=" + child.hashCode());
            }
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = getChildView(e);
            if (child != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(child);
                onItemClick(VH);
                //child.onTouchEvent(e);
                Log.e("Test", "onSingleTapUp hashCode=" + child.hashCode());
               /* child.setClickable(false);
                child.setFocusable(false);
                child.requestFocus();*/
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = getChildView(e);
            if (child != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(child);
                onItemLongClick(VH);
                //child.onTouchEvent(e);
                Log.e("Test", "onLongPress hashCode=" + child.hashCode());
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("Test", "onScroll distanceX");
            View child = getChildView(e1);
            if (child != null) {
               // child.onTouchEvent(e1);
                Log.e("Test", "onScroll hashCode=" + child.hashCode());
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private View getChildView(MotionEvent e) {
            return mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        }
    }

}
