package com.xiaozhang.sr.rv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xiaozhang.sr.R;


public class LoadingMoreView extends LinearLayout {

    private SimpleViewSwitcher progressCon;
    public final static int STATUS_LOADING = 0;
    public final static int STATUS_INIT = 1;
    public final static int STATUS_NOMORE = 2;
//    private TextView mText;


    public LoadingMoreView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        //设置内容居中
        setGravity(Gravity.CENTER);
        //设置布局的属性,
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 26, 0, 26);
        setLayoutParams(lp);
        //添加加载的动画
        progressCon = new SimpleViewSwitcher(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //默认的加载动画
        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(context);
        progressView.setIndicatorColor(context.getResources().getColor(R.color.orange_ed6d00));
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);
        addView(progressCon);
        //加载的字体
//        mText = new TextView(getContext());
//        mText.setText("正在加载...");
        //
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 0, 0, 0);

//        mText.setLayoutParams(layoutParams);
//        addView(mText);

        setStatus(STATUS_INIT);
    }

    //设置加载动画,和动画的颜色
    public void setProgressStyle(int style, int color) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(color);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }


    public void setStatus(int status) {
        switch (status) {
            case STATUS_LOADING:
                progressCon.setVisibility(View.VISIBLE);
//                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATUS_INIT:
//                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATUS_NOMORE:
//                mText.setText(getContext().getText(R.string.nomore_loading));
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
