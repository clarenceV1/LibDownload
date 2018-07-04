package com.cai.pullrefresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cai.pullrefresh.lib.PtrFrameLayout;
import com.cai.pullrefresh.lib.PtrUIHandler;
import com.cai.pullrefresh.lib.indicator.PtrIndicator;


/**
 * 刷新头部基类
 * Created by davy on 16/11/28.
 */

public abstract class PullRefreshLayout extends RelativeLayout implements PtrUIHandler {
    private static final String TIME_FILE_NAME = "pullrefresh_time_key";//保存刷新头部时间的文件名
    private String mLastUpdateTimeKey;//存储时间的key值

    public PullRefreshLayout(Context context) {
        super(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 自定义时间key
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * 默认时间key，类名
     *设置保存时间的key值
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }

    //保存时间
    private void saveLastRefreshTime() {
        getPref().edit().putLong(mLastUpdateTimeKey, System.currentTimeMillis()).commit();
    }

    /**
     * 获取存储的时间文案
     *
     * @return
     */
    public String getLastRefreshTime() {

        try {

            if (getContext() == null)
                return "刚刚";

            SharedPreferences pref = getPref();
            long time = pref.getLong(mLastUpdateTimeKey, 0);
            //时间
            if (time == 0) {
                return "第一次";
            }
            long currentTime = System.currentTimeMillis();
            long dur = currentTime - time;
            if (dur > Time.DAY * 30) {
                return "很久之前";
            } else if (dur > Time.DAY) {
                return dur / Time.DAY + "天前";
            } else if (dur > Time.HOUR) {
                return dur / Time.HOUR + "小时前";
            } else if (dur > Time.MINUTES) {
                return dur / Time.MINUTES + "分钟前";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "刚刚";
    }

    //
    private SharedPreferences getPref() {
        return getContext().getSharedPreferences(TIME_FILE_NAME, Context.MODE_PRIVATE);
    }

    public interface Time {
        long SECOND = 1000l;
        long MINUTES = 60 * SECOND;
        long HOUR = 60 * MINUTES;
        long DAY = 24 * HOUR;
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            saveLastRefreshTime();
        }
        onRefreshComplete(frame);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {//未达到刷新
                upCancelRefresh();
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {//达到抬手刷新
                upStartRefresh();
            }
        }
    }


    /**
     * 抬手取消刷新
     */
    protected abstract void upCancelRefresh();

    /**
     * 抬手开始刷新
     */
    protected abstract void upStartRefresh();

    /**
     * 刷新结束
     *
     * @param frame
     */
    protected abstract void onRefreshComplete(PtrFrameLayout frame);
}
