package com.xiaozhang.sr;

/**
 * Created by zhanghongqiang on 2016/11/25  上午10:55
 * ToDo:下拉刷新契约刷新
 */
public interface SwipeRefreshContract {
    //加载数据
    interface IFSwipeRefresh {
        void refreshData();
    }

    //代理者
    class SpPresenter {

        public SpPresenter(SwipeRefreshContract.IFSwipeRefresh loadData) {
            mRefresh = loadData;
        }

        SwipeRefreshContract.IFSwipeRefresh mRefresh = null;
    }
}
