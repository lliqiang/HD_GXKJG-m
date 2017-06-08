package com.hengda.smart.gxkjg.ui.adult;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.hengda.smart.gxkjg.entity.Exhibition;
import com.hengda.smart.gxkjg.entity.LikeNum;
//import ff.RatingBar;
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
import rx.Subscription;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/10/6 15:06
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class AGuideListFrgF3 extends Fragment {
private int unitNo;
    private List<Exhibit> exhibitList;
    Intent serviceIntent;
    private int lastNum=-1;
    private ImageView personImg;
    private Drawable drawable;
    private  Drawable drawable1;
    private List<Exhibition> exhibitionList=new ArrayList<>();
    public AGuideListFrgF3() {
    }

    @Bind(R.id.listView)
    ListView listView;
    LCommonAdapter<Exhibit> adapter;
    Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        serviceIntent = new Intent(getActivity(), BleNoService.class);
        if (HdAppConfig.getAutoPlay()){
//            getActivity().startService(serviceIntent);
            EventBus.getDefault().register(this);
        }

        unitNo=getArguments().getInt("unitNo");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_guide_list, container, false);
        exhibitList=new ArrayList<>();
        ButterKnife.bind(this, view);
        drawable= getResources().getDrawable(R.mipmap.list_heart);
        drawable1= getResources().getDrawable(R.mipmap.ic_like);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return view;
    }
    public static AGuideListFrgF3 getInstance(int unitNo) {
        AGuideListFrgF3 fragment = new AGuideListFrgF3();
        Bundle bundle = new Bundle();
        bundle.putInt("unitNo", unitNo);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setAdapter(adapter = new LCommonAdapter<Exhibit>(getActivity(),
                R.layout.item_exhibit_guide_list,exhibitList) {
            @Override
            public void convert(ViewHolder holder, Exhibit exhibit) {
                holder.setText(R.id.tvName, exhibit.getName());
                personImg = holder.getView(R.id.topperson);
                if (HdAppConfig.getLanguage().equals("ENGLISH")){
                    holder.setText(R.id.commend_start,"Rating");
                }else {
                    holder.setText(R.id.commend_start,"推荐指数");
                }
                if(exhibit.getType_ar()==5){
                    personImg.setVisibility(View.VISIBLE);
                }else {
                    personImg.setVisibility(View.INVISIBLE);
                }
                ImageView imageVie = holder.getView(R.id.imageView);
                RatingBar ratingBar=holder.getView(R.id.ratingBar);

                if (exhibit.getType_auto() == 3) {
                    ratingBar.setCount(4);

                } else {
                    ratingBar.setCount(3);
                }
                // TODO: 2016/12/27 获取收藏次数
                if (NetUtil.isConnected(getActivity())){

                    RequestApi.getInstance().preCount(new Subscriber<LikeNum>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(LikeNum likeNum) {
                            holder.setText(R.id.tvLikeNum,likeNum.getLike_num());

                        }
                    },exhibit.getFileNo());
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
                            if (status==1) {

                                ((TextView)holder.getView(R.id.tvLikeNum)).setCompoundDrawables(drawable,null,null,null);
                            } else {
                                ((TextView)holder.getView(R.id.tvLikeNum)).setCompoundDrawables(drawable1,null,null,null);
                            }
                        }
                    },HdAppConfig.getDeviceNo(),exhibit.getFileNo());
                }


                String path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/"+HdAppConfig.getUserType()+"/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + "_s_icon.png";
                if (HdAppConfig.isPictureExist(exhibit.getFileNo())){
                    Glide.with(AGuideListFrgF3.this)
                            .load(HdAppConfig.getPicturePath(exhibit.getFileNo()))
                            .placeholder(R.mipmap.default_l)
                            .into(imageVie);
                }else {
                    Glide.with(AGuideListFrgF3.this)
                            .load(path)
                            .placeholder(R.mipmap.default_l)
                            .into(imageVie);
                }
            }
        });


        loadAllExhibit();
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SingleDown.isExhibitExist(exhibitList.get(position).getFileNo())){
                    Intent intent = new Intent(getActivity(), Play.class);
                    Exhibit exhibit = adapter.getItem(position);
                    intent.putExtra("exhibit", exhibit);
                    //做展品浏览次数上传
                    if (NetUtil.isConnected(getActivity())){

                        RequestApi.getInstance().getCount(exhibit.getFileNo());
                    }
                    startActivity(intent);
                }
                else{



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
                            if (NetUtil.isConnected(getActivity())){

                                RequestApi.getInstance().getCount(exhibit.getFileNo());
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onLoadFailed() {
                            CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                            HResourceUtil.hideDownloadDialog();
                        }
                    }, exhibitList.get(position).getFileNo());


                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        for (int i = 0; i < exhibitList.size(); i++) {

            if (event.getBeaconNum() == exhibitList.get(i).getAutoNo()&& isReplay(event.getBeaconNum())) {
                setBleList(exhibitList);
                exhibitList.get(i).setType_ar(5);
                //// TODO: 2016/12/25 收到蓝牙好先弹框
                if (NetUtil.isConnected(getActivity())){

                    RequestApi.getInstance().putPositonInfo(HdAppConfig.getDeviceNo(),1,exhibitList.get(i).getAutoNo(),getActivity());
                }
                int finalI = i;

                if (exhibitList.get(i).getType_ar()==5){
//                personImg.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    if (listView!=null){
                        listView.setSelection(i);
                    }

                }
            }

        }


    }
    //重置蓝牙点
    public void setBleList(List<Exhibit> list){
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (serviceIntent!=null){

            getActivity().stopService(serviceIntent);
        }
    }

    public static AGuideListFrgF3 newInstance() {
        AGuideListFrgF3 fragment = new AGuideListFrgF3();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 加载所有展品
     */
    /**
     * 加载所有展品
     */
    private void loadAllExhibit() {
        Cursor cursor = HResDdUtil.getInstance().loadAllExhibit(3,unitNo);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibit exhibit=Exhibit.getExhibitInfo(cursor);
                exhibitList.add(exhibit);
            }
            cursor.close();
        }

    }





}
