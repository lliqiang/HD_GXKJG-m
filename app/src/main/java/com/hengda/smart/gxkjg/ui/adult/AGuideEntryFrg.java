package com.hengda.smart.gxkjg.ui.adult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.hengda.smart.common.rxbus.RxBus;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 08:43
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class AGuideEntryFrg extends BaseFragment {

    @Bind(R.id.ibFloor2)
    ImageButton ibFloor2;
    @Bind(R.id.ibFloor3)
    ImageButton ibFloor3;
    @Bind(R.id.ibFloor4)
    ImageButton ibFloor4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a_guide_entry, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        // TODO: 2017/6/5  
//        RxBus.getDefault().toObservable(Exhibit.class).subscribe(exhibit -> {
//            switch (exhibit.getFloor()) {
//                case 0:
//
//                    break;
//                case 2:
//                    openActivity(getActivity(), ListAdultActivity.class);
//                    break;
//                case 3:
//                    openActivity(getActivity(), ListF3AdultActivity.class);
//                    break;
//                case 4:
//                    openActivity(getActivity(), ListF4AdultActivity.class);
//                    break;
//                case 5:
//                    openActivity(getActivity(), ListF4AdultActivity.class);
//                    break;
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                Log.i("info",throwable.getMessage());
//            }
//        });

        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(Exhibit exhibit) {
        switch (exhibit.getFloor()) {
            case 0:

                break;
            case 2:
                openActivity(getActivity(), ListAdultActivity.class);
                break;
            case 3:
                openActivity(getActivity(), ListF3AdultActivity.class);
                break;
            case 4:
                openActivity(getActivity(), ListF4AdultActivity.class);
                break;
            case 5:
                openActivity(getActivity(), ListF4AdultActivity.class);
                break;
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibFloor2.setOnClickListener(v ->
                openActivity(getActivity(), ListAdultActivity.class));

        ibFloor3.setOnClickListener(v ->
                openActivity(getActivity(), ListF3AdultActivity.class));

        ibFloor4.setOnClickListener(v ->
                openActivity(getActivity(), ListF4AdultActivity.class));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public static AGuideEntryFrg newInstance() {
        AGuideEntryFrg fragment = new AGuideEntryFrg();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

}
