package com.xiaozhang.sr;

import android.support.v4.widget.SwipeRefreshLayout;


/**
 * Created by zhanghongqiang on 2016/11/25  上午10:53
 * ToDo:下拉刷新控件代理
 */
public class SwipeRefreshDelegate extends SwipeRefreshContract.SpPresenter {

    //刷新布局
    private SwipeRefreshLayout mSwipeLayout;
    //设置是否启动刷新
    private boolean mPullRefreshEnabled = true;

    public SwipeRefreshDelegate(SwipeRefreshContract.IFSwipeRefresh loadData) {
        super(loadData);
    }

    /**
     * @return
     */
    public static SwipeRefreshDelegate with(SwipeRefreshContract.IFSwipeRefresh mLoadData) {
        return new SwipeRefreshDelegate(mLoadData);
    }

    public SwipeRefreshDelegate s(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeLayout = swipeRefreshLayout;
        return this;
    }

    /**
     * @param pullRefresh 是否下拉
     * @return
     */
    public SwipeRefreshDelegate cancelRefresh(boolean pullRefresh) {
        this.mPullRefreshEnabled = pullRefresh;
        return this;
    }

    public SwipeRefreshDelegate build() {
        initSwipeRefreshLayout();
        return this;
    }


    //https://github.com/hanks-zyh/SwipeRefreshLayout
    private void initSwipeRefreshLayout() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
//        mSwipeLayout.setProgressViewOffset(true, 20, 200);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeLayout.setEnabled(false);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(
                R.color.tag_blue,
                R.color.light_blue,
                R.color.orange_ed6d00,
                android.R.color.holo_orange_light);

        // 通过 setEnabled(false) 禁用下拉刷新
//        mSwipeLayout.setEnabled(false);
        // 设定下拉圆圈的背景
//        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);
        //设置手势下拉刷新的监听
        //设置手势下拉刷新的监听
        if (mPullRefreshEnabled) {
            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    //网络请求数据
                    if (mRefresh != null) {
                        mSwipeLayout.setRefreshing(false);
                        mRefresh.refreshData();
                    }
                }
            });
        } else {
            mSwipeLayout.setEnabled(false);
        }
    }

    public void setRefreshing(Boolean refreshing) {
        if (mPullRefreshEnabled) {
            mSwipeLayout.setRefreshing(refreshing);
        }
    }

}
