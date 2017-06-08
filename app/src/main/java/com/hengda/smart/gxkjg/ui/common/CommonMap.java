package com.hengda.smart.gxkjg.ui.common;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.util.FragmentUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.group.MemberInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommonMap extends BaseActivity {

    @Bind(R.id.commonivBack)
    ImageView commonivBack;
    @Bind(R.id.commontvTitle)
    TextView commontvTitle;
    @Bind(R.id.commonflMapContainer)
    FrameLayout commonflMapContainer;
    @Bind(R.id.mapcommon)
    RelativeLayout mapcommon;
    @Bind(R.id.activity_common_map)
    LinearLayout activityCommonMap;
    private FragmentTransaction ft;
    private CommonF commonF;

    MemberInfo memberInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_map);
        ButterKnife.bind(this);
        commonivBack.setOnClickListener(view -> finish());
        if (HdAppConfig.getUserType().equals("adult")) {
            activityCommonMap.setBackgroundResource(R.mipmap.common);
        } else {
            activityCommonMap.setBackgroundResource(R.mipmap.bg_act_c_map);
        }
        memberInfo = (MemberInfo) getIntent().getSerializableExtra("map_name");
        int map_name =Integer.parseInt(memberInfo.getMap_id());
        if (memberInfo != null) {
            switch (map_name) {
                case 1:
                    showcMap(2);
                    break;
                case 2:
                    showcMap(3);
                    break;
                case 3:
                    showcMap(4);
                    break;
                case 4:
                    if (HdAppConfig.getUserType().equals("adult")) {
                        showcMap(4);
                    } else {
                        showcMap(5);
                    }

                    break;
            }


        }

    }

    private void showcMap(int floor) {
        if (floor == 5) {
            commontvTitle.setText("4L");
        } else {

            commontvTitle.setText(floor + "F");
        }
        ft = getFragmentManager().beginTransaction();

        if (commonF == null) {
            commonF = CommonF.newInstance(floor, memberInfo);
            FragmentUtil.addFragment(getFragmentManager(), R.id.commonflMapContainer, commonF, "cMapFrg", false, false);
        }
        ft.show(commonF);
        ft.commitAllowingStateLoss();
    }
}
