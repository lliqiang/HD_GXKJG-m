package com.hengda.smart.gxkjg.ui.adult;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hengda.smart.common.adapter.LCommonAdapter;
import com.hengda.smart.common.adapter.ViewHolder;
import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.autono.NumService;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.widget.RatingBar;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.LikeNum;;
import com.hengda.smart.gxkjg.entity.Pstatus;
import com.hengda.smart.play.Play;
import com.hengda.zwf.autonolibrary.BleNumService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import rx.Subscriber;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/10/6 15:06
 * 邮箱：tailyou@163.com
 * 描述：
 */

public class AGuideListFrg extends Fragment {
    private int unitNo;
    private List<Exhibit> exhibitList;
    private Intent serviceIntent;
    private int lastNum = -1;
    private ImageView personImg;
    private Drawable drawable;
    private Drawable drawable1;
    private View view;
    ViewHolder viewHolder;

    public AGuideListFrg() {
    }

    @Bind(R.id.listView)
    ListView listView;
    LCommonAdapter<Exhibit> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        serviceIntent = new Intent(getActivity(), BleNoService.class);
        if (HdAppConfig.getAutoPlay()) {
//            getActivity().startService(serviceIntent);
            EventBus.getDefault().register(this);
        }
        unitNo = getArguments().getInt("unitNo");

        drawable = getResources().getDrawable(R.mipmap.list_heart);
        drawable1 = getResources().getDrawable(R.mipmap.ic_like);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a_guide_list, container, false);
        exhibitList = new ArrayList<>();
        ButterKnife.bind(this, view);
        loadAllExhibit();

        listView.setAdapter(adapter = new LCommonAdapter<Exhibit>(getActivity(),
                R.layout.item_exhibit_guide_list, exhibitList) {
            @Override
            public void convert(ViewHolder holder, Exhibit exhibit) {
                viewHolder = holder;
                holder.setText(R.id.tvName, exhibit.getName());
                if (HdAppConfig.getLanguage().equals("ENGLISH")) {
                    holder.setText(R.id.commend_start, "Rating");
                } else {
                    holder.setText(R.id.commend_start, "推荐指数");
                }
                personImg = holder.getView(R.id.topperson);

                if (exhibit.getType_ar() == 5) {
                    personImg.setVisibility(View.VISIBLE);
//                    listView.setSelection(exhibitList.indexOf(exhibit));
                } else {
                    personImg.setVisibility(View.INVISIBLE);
                }

                ImageView imageVie = holder.getView(R.id.imageView);
                RatingBar ratingBar = holder.getView(R.id.ratingBar);

                // TODO: 2016/12/27 获取收藏次数
                if (NetUtil.isConnected(getActivity())) {

                    RequestApi.getInstance().preCount(new Subscriber<LikeNum>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(LikeNum likeNum) {

                            holder.setText(R.id.tvLikeNum, likeNum.getLike_num());
                        }
                    }, exhibit.getFileNo());
                    RequestApi.getInstance().lookAppreciate(new Subscriber<Pstatus>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Pstatus pstatus) {
                            int status = pstatus.getPstatus();
                            HdAppConfig.setAHeart(status);
                            if (status == 1) {

                                ((TextView) holder.getView(R.id.tvLikeNum)).setCompoundDrawables(drawable, null, null, null);
                            } else {
                                ((TextView) holder.getView(R.id.tvLikeNum)).setCompoundDrawables(drawable1, null, null, null);
                            }
                        }
                    }, HdAppConfig.getDeviceNo(), exhibit.getFileNo());
                }


// TODO: 2016/12/26 修改星
                if (exhibit.getType_auto() == 3) {
                    ratingBar.setCount(4);


                } else {
                    ratingBar.setCount(3);
                }

                String path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + "_s_icon.png";
                if (HdAppConfig.isPictureExist(exhibit.getFileNo())) {
                    Glide.with(AGuideListFrg.this)
                            .load(HdAppConfig.getPicturePath(exhibit.getFileNo()))
                            .placeholder(R.mipmap.default_l)
                            .into(imageVie);
                } else {
                    Glide.with(AGuideListFrg.this)
                            .load(path)
                            .placeholder(R.mipmap.default_l)
                            .into(imageVie);
                }


            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exhibit exhibit = exhibitList.get(position);

                if (SingleDown.isExhibitExist(exhibitList.get(position).getFileNo())) {
                    Intent intent = new Intent(getActivity(), Play.class);
                    intent.putExtra("exhibit", exhibit);
                    startActivity(intent);
                    //做展品浏览次数上传
                    if (NetUtil.isConnected(getActivity())) {

                        RequestApi.getInstance().getCount(exhibit.getFileNo());
                    }

                } else {

                    if (NetUtil.isConnected(getActivity())) {
                        HResourceUtil.showDownloadDialog(getActivity());
                        HResourceUtil.loadSingleExist(getActivity(), new ILoadListener() {
                            @Override
                            public void onLoadSucceed() {
                                Toast.makeText(getActivity(), R.string.down_success, Toast.LENGTH_SHORT).show();
                                HResourceUtil.hideDownloadDialog();
                                Intent intent = new Intent(getActivity(), Play.class);
                                Exhibit exhibit = exhibitList.get(position);
                                intent.putExtra("exhibit", exhibit);
                                //做展品浏览次数上传
                                RequestApi.getInstance().getCount(exhibit.getFileNo());
                                startActivity(intent);
                            }

                            @Override
                            public void onLoadFailed() {
                                CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                                HResourceUtil.hideDownloadDialog();
                            }
                        }, exhibit.getFileNo());
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });

        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        if (event != null) {
            for (int i = 0; i < exhibitList.size(); i++) {
                if (event.getBeaconNum() == exhibitList.get(i).getAutoNo() && isReplay(event.getBeaconNum())) {
                    setBleList(exhibitList);
                    exhibitList.get(i).setType_ar(5);
                    if (NetUtil.isConnected(getContext())) {

                        RequestApi.getInstance().putPositonInfo(HdAppConfig.getDeviceNo(), 1, exhibitList.get(i).getAutoNo(), getActivity());
                    }
                    int finalI = i;

                    if (exhibitList.get(i).getType_ar() == 5) {
                        adapter.notifyDataSetChanged();
                        if (listView != null) {
                            listView.setSelection(i);
                        }
                    }
                }
            }
        }


    }

    //重置蓝牙点
    public void setBleList(List<Exhibit> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setType_ar(0);
        }
    }

    //判断    隔一复收
    private boolean isReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastNum) {
            //如果不是-1的话返回true同时将蓝牙号置为-1
            lastNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    public static AGuideListFrg getInstance(int unitNo) {
        AGuideListFrg fragment = new AGuideListFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("unitNo", unitNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);


    }


    public static AGuideListFrg newInstance() {
        AGuideListFrg fragment = new AGuideListFrg();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载所有展品
     */
    private void loadAllExhibit() {
        Cursor cursor = HResDdUtil.getInstance().loadAllExhibit(2, unitNo);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);
                exhibitList.add(exhibit);
            }
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
        if (serviceIntent != null) {

            getActivity().stopService(serviceIntent);
        }
    }
}
