package com.hengda.smart.gxkjg.tool;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/8/19 10:26
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabTitles;
    private Context context;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitles, Context context) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
        this.context = context;
    }

    public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position % tabTitles.size());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

}
