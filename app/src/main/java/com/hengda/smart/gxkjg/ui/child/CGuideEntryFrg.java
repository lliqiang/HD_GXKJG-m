package com.hengda.smart.gxkjg.ui.child;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 08:43
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CGuideEntryFrg extends BaseFragment {

    @Bind(R.id.ibFloor2)
    ImageButton ibFloor2;
    @Bind(R.id.ibFloor3)
    ImageButton ibFloor3;
    @Bind(R.id.ibFloor4)
    ImageButton ibFloor4;
    @Bind(R.id.ibFloor4l)
    ImageButton ibFloor4l;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_c_guide_entry, container, false);
        ButterKnife.bind(this, view);

        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibFloor2.setOnClickListener(v -> {
            if (!HdAppConfig.isMapResExist()) {
                // TODO: 2016/12/24 下载地图资源
                HResourceUtil.showDownloadDialog(getActivity());
                HResourceUtil.loadMapRes(getActivity(), new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_success));
                        toMapAtc(2);
                    }

                    @Override
                    public void onLoadFailed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                    }
                });


            } else {
                toMapAtc(2);
            }

        });

        ibFloor3.setOnClickListener(v -> {
            if (!HdAppConfig.isMapResExist()) {
                // TODO: 2016/12/24 下载地图资源
                HResourceUtil.showDownloadDialog(getActivity());
                HResourceUtil.loadMapRes(getActivity(), new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_success));
                        toMapAtc(3);
                    }

                    @Override
                    public void onLoadFailed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                    }
                });
            } else {
                toMapAtc(3);
            }

        });
        ibFloor4.setOnClickListener(v -> {
            if (!HdAppConfig.isMapResExist()) {
                // TODO: 2016/12/24 下载地图资源
                HResourceUtil.showDownloadDialog(getActivity());
                HResourceUtil.loadMapRes(getActivity(), new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_success));
                        toMapAtc(4);
                    }

                    @Override
                    public void onLoadFailed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                    }
                });
            } else {
                toMapAtc(4);
            }

        });
//        ibFloor4l

        ibFloor4l.setOnClickListener(v -> {
            if (!HdAppConfig.isMapResExist()) {
                // TODO: 2016/12/24 下载地图资源
                HResourceUtil.showDownloadDialog(getActivity());
                HResourceUtil.loadMapRes(getActivity(), new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_success));
                        toMapAtc(5);
                    }

                    @Override
                    public void onLoadFailed() {
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                    }
                });
            } else {
                toMapAtc(5);
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static CGuideEntryFrg newInstance() {
        CGuideEntryFrg fragment = new CGuideEntryFrg();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 跳转至地图界面
     *
     * @param floor
     */
    private void toMapAtc(int floor) {
        Bundle bundle = new Bundle();
        bundle.putInt("FLOOR", floor);
        openActivity(getActivity(), MapChildActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
