package com.jcodecraeer.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {

    private LinearLayout mContainer;

    private ImageView mArrowImageView;

    private TextView mStatusTextView;

    private int mState = STATE_NORMAL;

    private AnimationDrawable mAnimation;

    public int mMeasuredHeight;

    public ArrowRefreshHeader(Context context, boolean scrollWhenFirst) {
        super(context);
        initView(scrollWhenFirst);
    }

    /**
     * @param context
     * @param attrs
     */
    public ArrowRefreshHeader(Context context, AttributeSet attrs, boolean scrollWhenFirst) {
        super(context, attrs);
        initView(scrollWhenFirst);
    }

    private void initView(boolean scrollWhenFirst) {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.listview_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView) findViewById(R.id.listview_header_arrow);
        mStatusTextView = (TextView) findViewById(R.id.refresh_status_textview);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();

        //隐藏商品logo,开启跑步动画

        mArrowImageView.setBackgroundResource(R.drawable.runningman);
        mAnimation = (AnimationDrawable) mArrowImageView.getBackground();

        /**
         * ToDo: 这一句是为了打开页面,就显示下拉刷新的效果
         */
        if (scrollWhenFirst) {
            smoothScrollTo(mMeasuredHeight);
        }

    }


    public void setProgressStyle(int style, int color) {

    }



    public void setState(int newState) {
        if (newState == mState) return;
        Log.i("state","newState = "+newState);
        Log.i("state","mState = "+mState);
        //判断当前最新状态
        switch (newState) {
            case STATE_NORMAL:
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText(R.string.listview_header_hint_normal);
                mArrowImageView.setBackgroundResource(R.drawable.runningman);
                mAnimation = (AnimationDrawable) mArrowImageView.getBackground();
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    if (!mAnimation.isRunning()) {
                        mAnimation.start();
                    }
                    mStatusTextView.setText(R.string.listview_header_hint_release);
                }
                break;
            case STATE_REFRESHING:
                mStatusTextView.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                mStatusTextView.setText(R.string.refresh_done);
                //停止动画
                if (mAnimation.isRunning()) {
                    mAnimation.stop();
                }
                mArrowImageView.setBackgroundResource(R.mipmap.a2a);
                break;
            default:
        }

        mState = newState;
    }

    public int getState() {
        return mState;
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }



}