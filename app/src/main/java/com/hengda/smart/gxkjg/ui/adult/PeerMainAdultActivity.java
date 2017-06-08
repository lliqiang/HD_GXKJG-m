package com.hengda.smart.gxkjg.ui.adult;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.adapter.RCommonAdapter;
import com.hengda.smart.common.adapter.ViewHolder;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.http.RequestSubscriber;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.group.MemberInfo;
import com.hengda.smart.gxkjg.ui.child.PeerMainChildActivity;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.CommonMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class PeerMainAdultActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.AivBack)
    ImageView ivBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvOnline)
    TextView tvOnline;
    @Bind(R.id.tvLabelName)
    TextView tvLabelName;
    @Bind(R.id.tvNickname)
    TextView tvNickname;
    @Bind(R.id.tvLabelUserId)
    TextView tvLabelUserId;
    @Bind(R.id.tvUserId)
    TextView tvUserId;
    @Bind(R.id.tvLabelGroupNo)
    TextView tvLabelGroupNo;
    @Bind(R.id.a_tvGroupNo)
    TextView tvGroupNo;
    @Bind(R.id.ivEditName)
    ImageView ivEditName;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MemberInfo memberIn;
    RCommonAdapter<MemberInfo> adapter;
    List<MemberInfo> mDatas = new ArrayList<>();
    @Bind(R.id.delete_group)
    TextView deleteGroup;
    private List<Exhibit> allExhibitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_peer_main);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(PeerMainAdultActivity.this);
        tvTitle.setTypeface(HdApplication.typefaceGxa);
        tvOnline.setTypeface(HdApplication.typefaceGxa);
        tvLabelName.setTypeface(HdApplication.typefaceGxa);
        tvNickname.setTypeface(HdApplication.typefaceGxa);
        tvLabelUserId.setTypeface(HdApplication.typefaceGxa);
        tvUserId.setTypeface(HdApplication.typefaceGxa);
        tvLabelGroupNo.setTypeface(HdApplication.typefaceGxa);
        tvGroupNo.setTypeface(HdApplication.typefaceGxa);
        deleteGroup.setTypeface(HdApplication.typefaceGxa);
        ivBack.setOnClickListener(this);
        deleteGroup.setOnClickListener(this);
        ivEditName.setOnClickListener(this);
        if (HdAppConfig.getAGroupNo() != 0) {
            deleteGroup.setVisibility(View.VISIBLE);
        } else {
            deleteGroup.setVisibility(View.INVISIBLE);
        }
        queryGroupMember();
        recyclerView.setLayoutManager(new LinearLayoutManager(PeerMainAdultActivity.this));
        recyclerView.setAdapter(adapter = new RCommonAdapter<MemberInfo>(PeerMainAdultActivity.this,
                R.layout.item_adult_group_member, mDatas) {
            @Override
            public void convert(ViewHolder holder, MemberInfo memberInfo) {
                holder.setTypeface(HdApplication.typefaceGxa, R.id.ausertxt);


                if (memberInfo.getNicename() != null) {
                    holder.setText(R.id.ausertxt, memberInfo.getNicename());
                } else {
                    holder.setText(R.id.ausertxt, memberInfo.getUser_login());
                }


                holder.getView(R.id.aimgLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!HdAppConfig.isMapResExist()) {
                            // TODO: 2016/12/24 下载地图资源
                            HResourceUtil.showDownloadDialog(PeerMainAdultActivity.this);
                            HResourceUtil.loadMapRes(PeerMainAdultActivity.this, new ILoadListener() {
                                @Override
                                public void onLoadSucceed() {
                                    CommonUtil.showToast(PeerMainAdultActivity.this, getString(R.string.down_success));
                                    HResourceUtil.hideDownloadDialog();
                                    HResourceUtil.showDownloadProgressDialog(PeerMainAdultActivity.this, getString(R.string.load_verson));
                                    RequestApi.getInstance().queryPositionMember(new Subscriber<MemberInfo>() {
                                        @Override
                                        public void onCompleted() {
                                            HResourceUtil.hideDownloadProgressDialog();
                                            Intent intent = new Intent(PeerMainAdultActivity.this, CommonMap.class);
                                            if (memberIn != null && memberIn.getAxis_x() != 0) {
                                                intent.putExtra("map_name", memberIn);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(PeerMainAdultActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            HResourceUtil.hideDownloadProgressDialog();
                                            Toast.makeText(PeerMainAdultActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onNext(MemberInfo memberInfo) {
                                            memberIn = memberInfo;
                                        }
                                    }, memberInfo.getUser_login());

                                }

                                @Override
                                public void onLoadFailed() {
                                    CommonUtil.showToast(PeerMainAdultActivity.this, getString(R.string.down_fail));
                                    HResourceUtil.hideDownloadDialog();
                                }
                            });
                        } else {
                            HResourceUtil.showDownloadProgressDialog(PeerMainAdultActivity.this, getString(R.string.load_verson));
                            RequestApi.getInstance().queryPositionMember(new Subscriber<MemberInfo>() {
                                @Override
                                public void onCompleted() {
                                    HResourceUtil.hideDownloadProgressDialog();
                                    Intent intent = new Intent(PeerMainAdultActivity.this, CommonMap.class);
                                    if (memberIn != null && memberIn.getAxis_x() != 0) {
                                        intent.putExtra("map_name", memberIn);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(PeerMainAdultActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(Throwable e) {
                                    HResourceUtil.hideDownloadProgressDialog();
                                    Toast.makeText(PeerMainAdultActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onNext(MemberInfo memberInfo) {
                                    memberIn = memberInfo;
                                }
                            }, memberInfo.getUser_login());
                        }
                    }
                });


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(HdAppConfig.getNickname())){

        }
        String nickname = TextUtils.isEmpty(HdAppConfig.getNickname()) ?
                HdAppConfig.getDeviceNo() : HdAppConfig.getNickname();
        tvNickname.setText(nickname + "");
        tvUserId.setText(HdAppConfig.getAGroupNo() + "");
        if (HdAppConfig.getAGroupNo() != 0) {
            tvGroupNo.setText(HdAppConfig.getGroupName() + "");
        }


    }


    /**
     * 查询群组成员
     */
    private void queryGroupMember() {
        DialogCenter.showProgressDialog(PeerMainAdultActivity.this, R.string.loading, false);
        RequestApi.getInstance().queryGroupMember(new RequestSubscriber<List<MemberInfo>>() {
            @Override
            public void failed(Throwable e) {
                DialogCenter.hideProgressDialog();
                CommonUtil.showToast(PeerMainAdultActivity.this, e.getMessage());
            }

            @Override
            public void succeed(List<MemberInfo> groupMembers) {
                DialogCenter.hideProgressDialog();
                mDatas.clear();
                mDatas.addAll(groupMembers);
                adapter.notifyDataSetChanged();
            }
        }, HdAppConfig.getDeviceNo());
    }

    /**
     * 显示修改昵称Dialog
     */
    private void showModifyNicknameDialog() {
        View root = View.inflate(PeerMainAdultActivity.this, R.layout.dialog_custom_view_edt, null);
        EditText edtNickname = (EditText) root.findViewById(R.id.editText);
        edtNickname.setTypeface(HdApplication.typefaceGxa);
        edtNickname.setText(HdAppConfig.getNickname());
        Selection.setSelection(edtNickname.getText(), edtNickname.getText().length());
        DialogCenter.showDialog(PeerMainAdultActivity.this, root, new DialogClickListener() {
            @Override
            public void p() {
                String nickname = edtNickname.getText().toString();
                nickname = nickname.replaceAll("(\r\n|\r|\n|\n\r)", "");
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtil.showToast(PeerMainAdultActivity.this, R.string.tip_input_nickname);
                } else {
                    modifyNickname(nickname.toString());
                }
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
            }
        }, new String[]{getString(R.string.modify_nickname),
                getString(R.string.submit),
                getString(R.string.close)});
    }

    /**
     * 修改昵称
     *
     * @param nickname
     */
    private void modifyNickname(String nickname) {
        if (NetUtil.isConnected(PeerMainAdultActivity.this)) {

            RequestApi.getInstance().modifyNickname(HdAppConfig.getDeviceNo(), nickname);
            DialogCenter.hideDialog();
            CommonUtil.showToast(PeerMainAdultActivity.this, R.string
                    .modify_nickname_succeed);
            HdAppConfig.setNickname(nickname);
            tvNickname.setText(nickname);
            queryGroupMember();


        } else {
            CommonUtil.showToast(PeerMainAdultActivity.this, R.string.net_not_available);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_group:
                HdAppConfig.setAGroupNo(0);
                RequestApi.getInstance().existGroup(HdAppConfig.getDeviceNo(), PeerMainAdultActivity.this);
                HdAppConfig.setNickname(null);
                deleteGroup.setVisibility(View.INVISIBLE);
                finish();
                break;
            case R.id.ivEditName:
                showModifyNicknameDialog();
                break;
            case R.id.AivBack:
                finish();
                break;
        }
    }

    private void loadExhibitByAutoNum(int autoNum) {
        allExhibitList.clear();
        Cursor cursor = HResDdUtil.getInstance().loadExhibitByAutoNo(autoNum);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);
                allExhibitList.add(exhibit);
            }
        } else {
            cursor.close();
        }
    }
}