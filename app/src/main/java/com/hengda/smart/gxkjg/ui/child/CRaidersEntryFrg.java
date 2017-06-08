package com.hengda.smart.gxkjg.ui.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.ui.common.AdviceActivity;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.gxkjg.ui.common.MapActivity;
import com.hengda.smart.gxkjg.ui.wdg.SlidingLayout;
import com.hengda.smart.gxkjg.ui.web.OpenAc;
import com.hengda.smart.gxkjg.ui.web.ThemeAcChild;
import com.hengda.smart.gxkjg.ui.web.WebAc;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 08:43
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CRaidersEntryFrg extends BaseFragment {


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
    @Bind(R.id.slideview)
    SlidingLayout slideview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_c_raiders_entryhas, container, false);
        ButterKnife.bind(this, view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tvPeer.setTypeface(HdApplication.typefaceGxc);
        tvTicket.setTypeface(HdApplication.typefaceGxc);
        tvRoute.setTypeface(HdApplication.typefaceGxc);
        tvTime.setTypeface(HdApplication.typefaceGxc);
        tvTheme.setTypeface(HdApplication.typefaceGxc);
        tvService.setTypeface(HdApplication.typefaceGxc);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPeer.setOnClickListener(v -> openActivity(getActivity(), CMyCompany.class));
        tvPeer.setOnClickListener(v -> {
            if (isGroupNoValid()) {
                openActivity(getActivity(), CMyCompany.class);
            } else {
                openActivity(getActivity(), PeerMainChildActivity.class);
            }
        });
        tvTicket.setOnClickListener(v -> {
            openActivity(getActivity(), WebAc.class);
        });
        tvRoute.setOnClickListener(v -> {
            openActivity(getActivity(), MapActivity.class);
        });
        tvService.setOnClickListener(v -> openActivity(getActivity(), AdviceActivity.class));
        tvTheme.setOnClickListener(v -> openActivity(getActivity(), ThemeAcChild.class));
        tvTime.setOnClickListener(v -> openActivity(getActivity(), OpenAc.class));
    }


    /**
     * 判断群组号是否有效
     *
     * @return
     */
    private boolean isGroupNoValid() {
        return HdAppConfig.getAGroupNo() == 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static CRaidersEntryFrg newInstance() {
        CRaidersEntryFrg fragment = new CRaidersEntryFrg();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
