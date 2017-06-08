package com.hengda.smart.gxkjg.ui.common;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/8/15 19:24
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class BaseFragment extends Fragment implements View.OnTouchListener {

    public View view;

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    protected void openActivity(Context context, Class<?> pClass) {
        openActivity(context, pClass, null, null);
    }

    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle) {
        openActivity(context, pClass, pBundle, null);
    }

    protected void openActivity(Context context, Class<?> pClass, String action) {
        openActivity(context, pClass, null, action);
    }

    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle, String action) {
        Intent intent = new Intent(context, pClass);
        if (action != null) {
            intent.setAction(action);
        }
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

}
