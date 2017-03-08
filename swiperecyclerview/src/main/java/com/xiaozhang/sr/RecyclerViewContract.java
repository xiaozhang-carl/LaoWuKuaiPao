package com.xiaozhang.sr;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午10:49
 * ToDo:列表的合约者
 */
public interface RecyclerViewContract {

    //加载数据
    interface IFLoadData {
        void loadData();
    }

    //适配器使用
    interface IFAdapter<T> {

        //可以根据数据类型来显示不同的item
        int getViewType(int position);

        //显示数据
        void updateView(@NonNull T data, @NonNull ViewDataBinding binding, int position);

        //这里的使用一定要注意,用第二个参数来判断
        ViewDataBinding createView(ViewGroup parent, int position);

    }

    //代理者
    abstract class RVPresenter<T> {

        public RVPresenter(IFLoadData loadData, IFAdapter adapter) {
            mLoadData = loadData;
            mAdapter = adapter;
        }

        //暴露给外界的接口是实现者
        RecyclerViewContract.IFAdapter mAdapter = null;

        RecyclerViewContract.IFLoadData mLoadData = null;


        public abstract void reLoadData();

        public abstract void render(List<T> list);

        public abstract void clearData();

        public abstract void notifyDataSetChanged();

        public abstract void notifyItemChanged(int position);

        public abstract void notifyItemRangeRemoved(T t);

        public abstract void notifyItemRangeInserted(int position, T t);


        //万用的适配器（需要结合databinding使用）
        class RVAdapter<T> extends RecyclerView.Adapter<RVViewHolder<T>> {

            ArrayList<T> mDatas = new ArrayList<>();

            @Override
            public RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //调用接口的方法
                ViewDataBinding binding = mAdapter.createView(parent, viewType);
                RVViewHolder viewHolder = new RVViewHolder(binding.getRoot());
                viewHolder.mViewDataBinding = binding;
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RVViewHolder holder, int position) {
                holder.setData(getItem(position),holder.getAdapterPosition());
            }

            private T getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getItemCount() {
                return mDatas != null ? mDatas.size() : 0;
            }

            @Override
            public int getItemViewType(int position) {
                //调用接口的方法
                return mAdapter.getViewType(position);
            }

            public void clearList(int positionStart) {
                //防止刷新闪烁的出现
                int itemCount = mDatas.size();
                mDatas.clear();
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void addNewList(int insertPosition, List<T> list) {
                //防止刷新闪烁的出现
                if (list != null && list.size() > 0) {
                    mDatas.addAll(list);
                    notifyItemRangeInserted(insertPosition, list.size());
                }
            }

        }

        /**
         * RecyclerView万用的适配器
         */
        class RVViewHolder<T> extends RecyclerView.ViewHolder {


            ViewDataBinding mViewDataBinding;

            public RVViewHolder(View itemView) {
                super(itemView);
            }

            public void setData(T data,int position) {
                //调用接口的方法
                mAdapter.updateView(data, mViewDataBinding,position);
            }
        }

    }
}
