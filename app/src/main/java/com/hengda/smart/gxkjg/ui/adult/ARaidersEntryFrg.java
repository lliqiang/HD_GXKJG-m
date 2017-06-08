package com.hengda.smart.gxkjg.ui.adult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.ui.common.AdviceActivity;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.gxkjg.ui.common.MapActivity;
import com.hengda.smart.gxkjg.ui.web.OpenAc;
import com.hengda.smart.gxkjg.ui.web.ThemeAc;
import com.hengda.smart.gxkjg.ui.web.WebAc;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 08:43
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ARaidersEntryFrg extends BaseFragment {

    @Bind(R.id.tvTicket)
    TextView tvTicket;
    @Bind(R.id.tvRoute)
    TextView tvRoute;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.tvPeer)
    TextView tvPeer;
    @Bind(R.id.tvTheme)
    TextView tvTheme;
    @Bind(R.id.tvService)
    TextView tvService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a_raiders_entry, container, false);
        ButterKnife.bind(this, view);

        tvPeer.setTypeface(HdApplication.typefaceGxa);
        tvTicket.setTypeface(HdApplication.typefaceGxa);
        tvRoute.setTypeface(HdApplication.typefaceGxa);
        tvTime.setTypeface(HdApplication.typefaceGxa);
        tvTheme.setTypeface(HdApplication.typefaceGxa);
        tvService.setTypeface(HdApplication.typefaceGxa);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPeer.setOnClickListener(v -> {
            if (isGroupNoValid()) {
                openActivity(getActivity(), PeerEntryAdultActivity.class);
            } else {

                openActivity(getActivity(), PeerMainAdultActivity.class);
            }
        });
        tvTicket.setOnClickListener(v -> {
            openActivity(getActivity(), WebAc.class);
        });
        tvRoute.setOnClickListener(v -> {
            openActivity(getActivity(), MapActivity.class);
        });
        tvService.setOnClickListener(v -> openActivity(getActivity(), AdviceActivity.class));
        tvTheme.setOnClickListener(v -> openActivity(getActivity(), ThemeAc.class));
        tvTime.setOnClickListener(v -> openActivity(getActivity(), OpenAc.class));
    }

    /**
     * 判断群组号是否有效
     *
     * @return
     */
    private boolean isGroupNoValid() {
        return HdAppConfig.getAGroupNo()==0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static ARaidersEntryFrg newInstance() {
        ARaidersEntryFrg fragment = new ARaidersEntryFrg();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

}
