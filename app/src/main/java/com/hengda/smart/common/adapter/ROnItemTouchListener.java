package com.hengda.smart.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/31 16:38
 * 邮箱：tailyou@163.com
 * 描述：RecyclerView 点击事件监听
 */
public abstract class ROnItemTouchListener implements RecyclerView.OnItemTouchListener {

    GestureDetector mGestureDetector;

    /**
     * 构造函数
     *
     * @param context
     * @param recyclerView
     */
    public ROnItemTouchListener(Context context, RecyclerView recyclerView) {
        mGestureDetector = new GestureDetector(context, new GestureDetector
                .SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    onItemLongClick(childView, recyclerView.getChildLayoutPosition(childView));
                }
            }
        });
    }

    /**
     * 点击
     *
     * @param view
     * @param position
     */
    public abstract void onItemClick(View view, int position);

    /**
     * 长按
     *
     * @param view
     * @param position
     */
    public abstract void onItemLongClick(View view, int position);

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            onItemClick(childView, rv.getChildLayoutPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
