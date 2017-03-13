package com.laowukuaipao;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.laowukuaipao.databinding.ActivityListviewBinding;
import com.laowukuaipao.databinding.Item1Binding;
import com.laowukuaipao.jd.JdRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by zhanghongqiang on 2017/3/9  下午4:08
 * ToDo:
 */
public class ListViewActivity extends Activity implements ListViewContract.IFAdapter<String>, ListViewContract.IFLoadData {

    private ActivityListviewBinding mBinding;

    private ListViewPresenter mListViewPresenter;

    /**
     * 下拉刷新
     */
    JdRefreshLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview);
        mListViewPresenter = ListViewPresenter.with(this, this)
                .listView(mBinding.listView)
                .build();
        mLayout = (JdRefreshLayout) findViewById(R.id.test_recycler_view_frame);

        mLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
            }
        });
        mLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                mLayout.autoRefresh(true);
            }
        }, 150);
        loadData();

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
                ToastUtil.show(ListViewActivity.this, "position=" + position);
            }
        });
    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int position) {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_1, parent, false);
    }

    List<String> list = new ArrayList<>();


    @Override
    public void loadData() {
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
                mListViewPresenter.render(list);
                mLayout.refreshComplete();
            }
        }.execute();

    }

}