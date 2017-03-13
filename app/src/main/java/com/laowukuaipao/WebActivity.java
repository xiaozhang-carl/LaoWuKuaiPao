package com.laowukuaipao;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laowukuaipao.databinding.ActivityWebBinding;
import com.xiaozhang.sr.SwipeRefreshContract;
import com.xiaozhang.sr.SwipeRefreshDelegate;

public class WebActivity extends AppCompatActivity implements SwipeRefreshContract.IFSwipeRefresh{

    private ActivityWebBinding mBinding;
    //下拉刷新
    private SwipeRefreshDelegate mSwipeRefreshDelegate;
    //webview
    private WebView webView;
    //加载地址
    private String url = "http://www.jianshu.com/u/6187ecd8a511";
    //标题
    private String title = "hha";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        initView();
        refreshData();
    }


    /**
     * 初始化控件
     */
    private void initView() {

        webView = mBinding.webView;

        webView.getSettings().setJavaScriptEnabled(true);

        //总是从网路获取数据
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChrome());
        //调试webview ，chrome浏览器输入chrome://inspect，看到设备点 inspect 即可
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        //
        mSwipeRefreshDelegate = SwipeRefreshDelegate.with(this).s(mBinding.swipeRefreshLayout).build();
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void refreshData() {
        if (TextUtils.isEmpty(webView.getUrl())) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl(webView.getUrl());
        }
    }

    private class WebChrome extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            // 设置标题
            mBinding.setTitle(title);
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress >= 100) {//加载成功或网络失败：newProgress=100
                mSwipeRefreshDelegate.setRefreshing(false);
            }
        }

    }
}
