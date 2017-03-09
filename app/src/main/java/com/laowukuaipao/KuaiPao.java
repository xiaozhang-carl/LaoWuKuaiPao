package com.laowukuaipao;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.RecyclerViewContract;
import com.jcodecraeer.xrecyclerview.XRecyclerViewDelegate;
import com.laowukuaipao.databinding.ActivityRecyclerViewBinding;
import com.laowukuaipao.databinding.Item1Binding;
import com.laowukuaipao.databinding.ViewEmptyShopcartBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 2017/3/8  下午8:37
 * ToDo:
 */
public class KuaiPao extends Activity implements RecyclerViewContract.IFAdapter<String>, RecyclerViewContract.IFLoadData {

    private ActivityRecyclerViewBinding mBinding;

    private XRecyclerViewDelegate mXRecyclerViewDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        //空布局的设置
        ViewEmptyShopcartBinding emptyBinding =
                DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_empty_shopcart, null, false);
        mXRecyclerViewDelegate = mXRecyclerViewDelegate.with(this, this)
                .recyclerView(mBinding.XRecyclerView, mBinding.flContent, emptyBinding.getRoot())
                .build();
        mXRecyclerViewDelegate.reLoadData();

    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public void updateView(@NonNull String data, @NonNull ViewDataBinding binding, final int position) {
        Item1Binding binding1 = (Item1Binding) binding;
        binding1.setStr(data);
        binding1.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(KuaiPao.this, "position=" + position);
            }
        });
    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int position) {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_1, parent, false);
    }

    List<String> list = new ArrayList<>();
    int page = 0;

    @Override
    public void loadData() {
        mXRecyclerViewDelegate.setPage(page++);
        list = new ArrayList<>();
        list.add("a1");
        list.add("a2");
        list.add("a3");
        list.add("a4");
        list.add("a5");
        list.add("a6");
        list.add("a7");


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mXRecyclerViewDelegate.render(list);
            }
        }.execute();

    }
}