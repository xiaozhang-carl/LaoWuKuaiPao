package com.xiaozhang.sr;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class RecyclerViewDelegate<T> extends RecyclerViewContract.RVPresenter {


    //xml里面的列表
    private RecyclerView mRecyclerView;

    //万用的适配器
    private RVAdapter mAdapter;


    public RecyclerViewDelegate(RecyclerViewContract.IFLoadData mLoadData, RecyclerViewContract.IFAdapter F) {
        super(mLoadData, F);
    }

    /**
     * @return
     */
    public static RecyclerViewDelegate with(RecyclerViewContract.IFLoadData mLoadData, RecyclerViewContract.IFAdapter F) {
        return new RecyclerViewDelegate(mLoadData, F);
    }

    public RecyclerViewDelegate recyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        linearLayoutManager();
        return this;
    }


//    public RecyclerViewDelegate fullRecyclerView(RecyclerView recyclerView) {
//        this.mRecyclerView = recyclerView;
//        fullLinearLayoutManager();
//        return this;
//    }
//
//    private void fullLinearLayoutManager() {
//        //默认的layoutManager
//        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mRecyclerView.getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        //设置layoutManager
//        mRecyclerView.setLayoutManager(layoutManager);
//    }


    /**
     * @param spanCount 网格布局的格数
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount) {
        this.mRecyclerView = recyclerView;
        gridLayoutManager(spanCount);
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount, int orientation) {
        this.mRecyclerView = recyclerView;
        staggeredGridLayoutManager(spanCount, orientation);
        return this;
    }


    private void linearLayoutManager() {
        //默认的layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * ToDo:网格列表
     *
     * @param spanCount
     */
    private void gridLayoutManager(int spanCount) {
        //GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), spanCount);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * ToDo:交错网格
     *
     * @param spanCount
     */
    private void staggeredGridLayoutManager(int spanCount, int orientation) {
        // 交错网格布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public RecyclerViewDelegate<T> build() {

        //新建适配器
        mAdapter = new RVAdapter();
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //取消动画
//        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        return this;
    }

    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            //数据如果为空的话
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
    public void notifyItemChanged(int position) {
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.notifyItemRangeChanged(position, 1);
        }
    }

    @Override
    public void notifyItemRangeRemoved(Object o) {
        if (o == null) {
            return;
        }
        T t = (T) o;
        int position = indexOf(t);
        if (mAdapter != null) {
            getDataList().remove(position);
            mAdapter.notifyItemRangeRemoved(position, 1);

        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    public void notifyItemRangeRemoved(int position) {
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            //从数据源中移除数据
            getDataList().remove(position);
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            mAdapter.notifyItemRangeRemoved(position, 1);
        }
    }

    //获取数据列表
    public List<T> getDataList() {
        return mAdapter.mDatas;
    }


    public int indexOf(T t) {
        return getDataList().indexOf(t);
    }


    //重新加载数据
    @Override
    public void reLoadData() {
        if (mLoadData != null) {
            mLoadData.loadData();
        }
    }

    
    public void render(List newDatas) {
        if (newDatas==null){
            return;
        }
        //没有数据,显示空数据
        if (newDatas.size() == 0) {
            clearData();
            return;
        } else {
            //有数据的话,清空原来的数据,防止数据重复添加
            clearData();

        }
        //加入新的数据
        mAdapter.addNewList(0, newDatas);
    }

    public void clearData() {
        if (mAdapter != null) {
            mAdapter.clearList(0);
        }
    }

    @Override
    public void notifyItemRangeInserted(int position, Object o) {
        if (position < 0) {
            return;
        }
        T t = (T) o;
        if (mAdapter != null) {
            getDataList().add(position, t);
            mAdapter.notifyItemRangeInserted(position, 1);
        }
    }


}
