package com.hengda.smart.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/31 16:04
 * 邮箱：tailyou@163.com
 * 描述：ListView CommonAdapter
 */
public abstract class LCommonAdapter<T>
        extends BaseAdapter
        implements Action1<List<T>> {

    protected Context mContext;
    protected List<T> mDatas = Collections.emptyList();
    protected LayoutInflater mInflater;
    protected int layoutId;

    public LCommonAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }

    public LCommonAdapter(Context context, int layoutId, List<T> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder   holder= ViewHolder.get(mContext, convertView, parent,
                layoutId, position);

        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public void call(List<T> ts) {
        this.mDatas = ts;
        notifyDataSetChanged();
    }

}
