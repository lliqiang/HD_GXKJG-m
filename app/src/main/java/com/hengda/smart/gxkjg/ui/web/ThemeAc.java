package com.hengda.smart.gxkjg.ui.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hengda.smart.common.adapter.LCommonAdapter;
import com.hengda.smart.common.adapter.ViewHolder;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.util.AppUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.AcInfo;
import com.hengda.smart.gxkjg.ui.common.QringAc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class ThemeAc extends AppCompatActivity {


    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.lv_theme)
    ListView lvTheme;
    private List<AcInfo> acInfoList;
    LCommonAdapter<AcInfo> adapter;
    private ImageView topLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);
        acInfoList = new ArrayList<>();

        StatusBarCompat.translucentStatusBar(this);
        ivBack.setOnClickListener(v -> finish());
        if (HdAppConfig.getUserType().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);

        }
        if (NetUtil.isConnected(ThemeAc.this)) {

            RequestApi.getInstance().getThemeInfo(new Subscriber<List<AcInfo>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<AcInfo> acInfos) {

                    acInfoList = acInfos;
                    Collections.sort(acInfoList, new Comparator<AcInfo>() {
                        @Override
                        public int compare(AcInfo acInfo, AcInfo t1) {
                            Date date1 = AppUtil.stringToDate(acInfo.getHoldy() + "-" + acInfo.getHoldm());
                            Date date2 = AppUtil.stringToDate(t1.getHoldy() + "-" + t1.getHoldm());
                            // 对日期字段进行升序，如果欲降序可采用after方法
                            if (date1.before(date2)) {
                                return 1;
                            }
                            return -1;


                        }
                    });
                    lvTheme.setAdapter(adapter = new LCommonAdapter<AcInfo>(ThemeAc.this, R.layout.lv_item_theme, acInfoList) {
                        @Override
                        public void convert(ViewHolder holder, AcInfo acInfo) {
                            topLast = holder.getView(R.id.latest);
                            if (acInfo != null) {


                                if (acInfo.getIsLast() == 1) {
                                    topLast.setVisibility(View.VISIBLE);
                                } else {
                                    topLast.setVisibility(View.INVISIBLE);
                                }
                                holder.setText(R.id.txt_theme, acInfo.getTitle());
                                holder.getConvertView().setOnClickListener(v -> {
                                    Intent intent = new Intent(ThemeAc.this, QringAc.class);
                                    intent.putExtra("AcInfo", acInfo);
                                    startActivity(intent);

                                });
                            }
                        }
                    });

                    for (int i = 0; i < acInfoList.size(); i++) {
                        if (i <= 2) {
                            acInfoList.get(i).setIsLast(1);
                        } else {
                            acInfoList.get(i).setIsLast(0);
                        }
                    }
                    adapter.call(acInfoList);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
