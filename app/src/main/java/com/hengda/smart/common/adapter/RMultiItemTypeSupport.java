package com.hengda.smart.common.adapter;

public interface RMultiItemTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}