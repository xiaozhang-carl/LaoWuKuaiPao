package com.jcodecraeer.xrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理
 */
public class XRecyclerViewDelegate<T> extends RecyclerViewContract.RVPresenter<T> {


    //列表的父布局
    FrameLayout mFrameLayout;

    //列表空布局的现实
    View mEmptyView;

    //xml里面的列表
    private XRecyclerView mRecyclerView;

    //万用的适配器
    private RVAdapter mAdapter;

    //头布局
    private View mHeaderView;

    //底部布局
    private View mFooterView;

    //分页,从0开始
    private int mPage = 0;

    //每一页的item个数,默认20条
    private int pageSize = 20;


    public XRecyclerViewDelegate(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        super(L, F);
    }

    /**
     * @param L 加载数据,可以为空
     * @param F 适配器的实现接口,必须实现
     * @return
     */
    public static XRecyclerViewDelegate with(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        return new XRecyclerViewDelegate(L, F);
    }

    /**
     * 普通的正常的列表显示
     *
     * @param recyclerView
     * @return
     */
    public XRecyclerViewDelegate recyclerView(@NonNull XRecyclerView recyclerView) {
        initVariable(recyclerView, null, null);
        linearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param recyclerView
     * @param frameLayout
     * @param emptyView
     * @return
     */
    public XRecyclerViewDelegate recyclerView(@NonNull XRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        initVariable(recyclerView, frameLayout, emptyView);
        linearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param recyclerView
     * @return
     */
    public XRecyclerViewDelegate fullRecyclerView(@NonNull XRecyclerView recyclerView) {
        initVariable(recyclerView, null, null);
        fullLinearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param recyclerView
     * @param frameLayout
     * @param emptyView
     * @return
     */
    public XRecyclerViewDelegate fullRecyclerView(@NonNull XRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        initVariable(recyclerView, frameLayout, emptyView);
        fullLinearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }


    /**
     * 初始化列表,列表父布局,需要显示的空布局
     *
     * @param recyclerView
     * @param frameLayout
     * @param emptyView
     */
    private void initVariable(@NonNull XRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        mRecyclerView = recyclerView;
        if (frameLayout != null && emptyView != null) {
            mFrameLayout = frameLayout;
            mEmptyView = emptyView;
            //空布局添加到父布局
            mFrameLayout.addView(mEmptyView);
        }
    }


    /**
     * @param spanCount 网格布局的格数
     */
    public XRecyclerViewDelegate<T> recyclerView(XRecyclerView recyclerView, int spanCount, FrameLayout frameLayout, View emptyView) {
        initVariable(recyclerView, frameLayout, emptyView);
        gridLayoutManager(spanCount);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public XRecyclerViewDelegate recyclerView(XRecyclerView recyclerView, int spanCount) {
        initVariable(recyclerView, null, null);
        gridLayoutManager(spanCount);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public XRecyclerViewDelegate recyclerView(XRecyclerView recyclerView, int spanCount, int orientation) {
        initVariable(recyclerView, null, null);
        staggeredGridLayoutManager(spanCount, orientation);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public XRecyclerViewDelegate<T> recyclerView(XRecyclerView recyclerView, FrameLayout frameLayout, View emptyView, int spanCount, int orientation) {
        initVariable(recyclerView, frameLayout, emptyView);
        staggeredGridLayoutManager(spanCount, orientation);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }


    //加载动画http://blog.csdn.net/developer_jiangqq/article/details/49612399
    private void setRefreshLoadingMoreProgressStyle() {
        //头部加载小圆点,主题黄
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //底部小方块
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
    }


    /**
     * 设置上拉,下拉的加载事件
     */
    private void setRefreshLoadMore() {
        //设置下拉和加在更多的事件
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新,刷新成功后,清空原有数据
                //页面设置为第一页
                if (mLoadData != null) {
                    mPage = 0;
                    hideEmptyView();
                    mLoadData.loadData();
                }
            }

            @Override
            public void onLoadMore() {
                if (mLoadData != null) {
                    //加载更多
                    mLoadData.loadData();
                }
            }
        });
    }

    private void linearLayoutManager() {
        //默认的layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void fullLinearLayoutManager() {
        //默认的layoutManager

    }

    /**
     *
     */
    private void gridLayoutManager(int spanCount) {
        //GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), spanCount);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * @param spanCount
     */
    private void staggeredGridLayoutManager(int spanCount, int orientation) {
        // 交错网格布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public XRecyclerViewDelegate headerView(@NonNull View headerView) {
        this.mHeaderView = headerView;
        return this;
    }

    public XRecyclerViewDelegate footerView(@NonNull View footerView) {
        this.mFooterView = footerView;
        return this;
    }


    /**
     * @param pullRefresh 头部是否下拉
     * @param loadingMore 底部加载更多
     * @return
     */
    public XRecyclerViewDelegate<T> cancelRefresh(boolean pullRefresh, boolean loadingMore) {
        if (mRecyclerView != null) {
            mRecyclerView.setPullRefreshEnabled(pullRefresh);
            mRecyclerView.setLoadingMoreEnabled(loadingMore);
        }
        return this;
    }

    public XRecyclerViewDelegate<T> build() {
        //添加头部
        if (this.mHeaderView != null) {
            mRecyclerView.addHeaderView(mHeaderView);
        }
        //添加尾部
        if (this.mFooterView != null) {
            mRecyclerView.addFootView(mFooterView);
        }
        //新建适配器
        mAdapter = new RVAdapter();
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //取消动画
//        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        return this;
    }

    //下一页
    public int nextPage() {
        return ++mPage;
    }


    public int getPage() {
        return mPage;
    }

    //获取数据列表
    public List<T> getDataList() {
        return mAdapter.mDatas;
    }

    //返回每页的条目
    public int getPageSize() {
        return pageSize;
    }

    //设置每页的条目
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //设置页码,配合分页使用
    public void setPage(int page) {
        this.mPage = page;
    }

    //这是为了加loadingDialog
    public void loadData() {
        if (mLoadData != null) {
            mLoadData.loadData();
        }
    }

    //加载新的数据
    public void reLoadData() {
        if (mLoadData != null) {
            mPage = 0;
            mLoadData.loadData();
        }
    }

    //加入新的数据
    @Override
    public void render(List list) {
        //刷新完成,隐藏进度条...
        refreshComplete();
        if (list == null) {
            return;
        }
        //下拉刷新,多次请求首页的话,清空数据
        if (mPage == 1 || mPage == 0) {
            //首先清空原有数据
            //首先清空原有数据
            clearData();
            if (list.size() != 0) {
                //有数据的话,清空原来的数据,防止数据重复添加。
                getDataList().addAll(list);
            }
            mAdapter.notifyDataSetChanged();
            showEmptyView();
        } else {
            //加入新的数据
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            int position = getDataList().size();
            if (mHeaderView == null) {
                mAdapter.addNewList(position + 1, list);
            } else {
                mAdapter.addNewList(position + 2, list);
            }
        }

    }


    //刷新完成,隐藏进度条...
    public void refreshComplete() {
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();
    }

    //清全部数据
    public void clearData() {
        if (mAdapter != null) {
            if (mHeaderView == null) {
                mAdapter.clearList(1);
            } else {
                mAdapter.clearList(2);
            }
        }
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            //数据如果为空的话,现实占位图
            if (getDataList().size() == 0) {
                showEmptyView();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyItemChanged(int position) {
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeChanged(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeChanged(position + 2, 1);
            }
        }
    }

    @Override
    public void notifyItemRangeRemoved(T t) {
        int position = indexOf(t);
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            //从数据源中移除数据
            getDataList().remove(position);
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeRemoved(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeRemoved(position + 2, 1);
            }
            //如果没有数据了,就显示空数据
            if (getDataList().size() == 0) {
                showEmptyView();
            }
        }
    }

    /**
     * @param position 需要加入到列表的位置
     * @param o        列表的item数据
     */

    @Override
    public void notifyItemRangeInserted(int position, Object o) {
        if (position < 0) {
            return;
        }
        T t = (T) o;
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            getDataList().add(position, t);
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeInserted(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeInserted(position + 2, 1);
            }
        }
    }


    public int indexOf(T t) {
        return getDataList().indexOf(t);
    }

    public void showEmptyView() {
        if (mEmptyView != null) {
            if (getDataList().size() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }

    private void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

}
