package com.xiaozhang.sr;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.xiaozhang.sr.rv.LoadingMoreView;

/**
 * Created by zhanghongqiang on 2017/2/7  上午10:43
 * ToDo:RecyclerView的滚动监听，用来处理列表的下拉刷新和上拉加载更多的逻辑
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    //瀑布流使用的
    private int[] mLastPostions = null;
    //dy>0,表示用户向上滑动
    private int dy = 0;
    //谷歌下拉刷新的布局
    private SwipeRefreshLayout mSwipeLayout;
    //加载更多的布局
    private View mLoadingView;
    //是否在加载数据中
    private boolean mLoading = false;
    //第一个完全显示的列表item
    private int mFirstVisibleItemPosition;
    //最后一个完全显示的列表item
    private int mLastVisibleItemPosition;
    //加载数据的回调
    private RecyclerViewContract.IFLoadData mLoadData;


    public RecyclerViewScrollListener(SwipeRefreshLayout swipeLayout, RecyclerViewContract.IFLoadData loadData) {
        mSwipeLayout = swipeLayout;
        mLoadData = loadData;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //RecycleView 显示的条目数
        int visibleCount = layoutManager.getChildCount();
        //显示数据总数
        int totalCount = layoutManager.getItemCount();
        //判断RecyclerView的状态,区分下拉和上拉
        if (!mSwipeLayout.isRefreshing() && dy > 0) {
            if (visibleCount > 0
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItemPosition >= totalCount - 1
                    && !mLoading && mLoadData != null) {
                mLoading = true;
                //加载更多的显示
                if (mLoadingView instanceof LoadingMoreView && !mSwipeLayout.isRefreshing()) {
                    LoadingMoreView LoadingMoreView = (LoadingMoreView) mLoadingView;
                    LoadingMoreView.setStatus(LoadingMoreView.STATUS_LOADING);
                } else {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                //加载数据
                mLoadData.loadData();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //判断下列表布局管理器
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            mFirstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            mLastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            LinearLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            mFirstVisibleItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            mLastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (mLastPostions == null) {
                mLastPostions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPostions);
            mLastVisibleItemPosition = findMax(mLastPostions);
        }
        //dy>0,表示用户向上滑动
        this.dy = dy;
//        Log.i("123", "dy= " + dy);
        onScrolled(mFirstVisibleItemPosition, mLastVisibleItemPosition);
    }

    //用户可以通过调用这个方法，获取列表的滚动状态
    public void onScrolled(int firstVisibleItemPosition, int lastVisibleItemPosition) {
    }


    /**
     * 当是瀑布流时，获取到的是每一个瀑布最下方显示的条目，通过条目进行对比
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }
}
