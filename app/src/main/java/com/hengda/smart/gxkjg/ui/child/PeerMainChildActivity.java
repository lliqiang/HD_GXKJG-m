package com.hengda.smart.gxkjg.ui.child;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.TextUtils;
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
import com.hengda.smart.gxkjg.ui.adult.PeerMainAdultActivity;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.CommonMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class PeerMainChildActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.civBack)
    ImageView civBack;
    @Bind(R.id.ctvTitle)
    TextView ctvTitle;
    @Bind(R.id.cdelete_group)
    TextView cdeleteGroup;
    @Bind(R.id.ctvOnline)
    TextView ctvOnline;
    @Bind(R.id.ctvLabelName)
    TextView ctvLabelName;
    @Bind(R.id.ctvNickname)
    TextView ctvNickname;
    @Bind(R.id.ctvLabelUserId)
    TextView ctvLabelUserId;
    @Bind(R.id.ctvUserId)
    TextView ctvUserId;
    @Bind(R.id.ctvLabelGroupNo)
    TextView ctvLabelGroupNo;
    @Bind(R.id.ctvGroupNo)
    TextView ctvGroupNo;
    @Bind(R.id.civEditName)
    ImageView civEditName;
    @Bind(R.id.crecyclerView)
    RecyclerView crecyclerView;
    RCommonAdapter<MemberInfo> adapter;
    List<MemberInfo> mDatas = new ArrayList<>();
    private List<Exhibit> allExhibitList = new ArrayList<>();
    private MemberInfo memberIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_peer_main);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(PeerMainChildActivity.this);

        ctvTitle.setTypeface(HdApplication.typefaceGxc);
        ctvOnline.setTypeface(HdApplication.typefaceGxc);
        ctvLabelName.setTypeface(HdApplication.typefaceGxc);
        ctvNickname.setTypeface(HdApplication.typefaceGxc);
        ctvLabelUserId.setTypeface(HdApplication.typefaceGxc);
        ctvUserId.setTypeface(HdApplication.typefaceGxc);
        ctvLabelGroupNo.setTypeface(HdApplication.typefaceGxc);
        ctvGroupNo.setTypeface(HdApplication.typefaceGxc);
        cdeleteGroup.setTypeface(HdApplication.typefaceGxc);
        if (HdAppConfig.getAGroupNo() != 0) {
            cdeleteGroup.setVisibility(View.VISIBLE);
        } else {
            cdeleteGroup.setVisibility(View.INVISIBLE);
        }
        cdeleteGroup.setOnClickListener(this);
        civEditName.setOnClickListener(this);
        civBack.setOnClickListener(this);
        crecyclerView.setLayoutManager(new LinearLayoutManager(PeerMainChildActivity.this));


        crecyclerView.setAdapter(adapter = new RCommonAdapter<MemberInfo>(PeerMainChildActivity.this,
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
                                        HResourceUtil.showDownloadDialog(PeerMainChildActivity.this);
                                        HResourceUtil.loadMapRes(PeerMainChildActivity.this, new ILoadListener() {
                                            @Override
                                            public void onLoadSucceed() {
                                                CommonUtil.showToast(PeerMainChildActivity.this, getString(R.string.down_success));
                                                HResourceUtil.hideDownloadDialog();
                                                HResourceUtil.showDownloadProgressDialog(PeerMainChildActivity.this, getString(R.string.load_verson));
                                                RequestApi.getInstance().queryPositionMember(new Subscriber<MemberInfo>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        HResourceUtil.hideDownloadProgressDialog();
                                                        Intent intent = new Intent(PeerMainChildActivity.this, CommonMap.class);
                                                        if (memberIn != null && memberIn.getAxis_x() != 0) {
                                                            intent.putExtra("map_name", memberIn);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(PeerMainChildActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        HResourceUtil.hideDownloadProgressDialog();
                                                        Toast.makeText(PeerMainChildActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onNext(MemberInfo memberInfo) {
                                                        memberIn = memberInfo;
                                                    }
                                                }, memberInfo.getUser_login());

                                            }

                                            @Override
                                            public void onLoadFailed() {
                                                CommonUtil.showToast(PeerMainChildActivity.this, getString(R.string.down_fail));
                                                HResourceUtil.hideDownloadDialog();
                                            }
                                        });
                                    } else {
                                        HResourceUtil.showDownloadProgressDialog(PeerMainChildActivity.this, getString(R.string.load_verson));
                                        RequestApi.getInstance().queryPositionMember(new Subscriber<MemberInfo>() {
                                            @Override
                                            public void onCompleted() {
                                                HResourceUtil.hideDownloadProgressDialog();
                                                Intent intent = new Intent(PeerMainChildActivity.this, CommonMap.class);
                                                if (memberIn != null && memberIn.getAxis_x() != 0) {
                                                    intent.putExtra("map_name", memberIn);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(PeerMainChildActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onError(Throwable e) {
                                                HResourceUtil.hideDownloadProgressDialog();
                                                Toast.makeText(PeerMainChildActivity.this, "暂无位置信息", Toast.LENGTH_SHORT).show();
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
        String nickname = TextUtils.isEmpty(HdAppConfig.getNickname()) ?
                HdAppConfig.getDeviceNo() : HdAppConfig.getNickname();
        ctvNickname.setText(nickname);
        ctvUserId.setText(HdAppConfig.getAGroupNo() + "");
        ctvGroupNo.setText(HdAppConfig.getGroupName() + "");
        queryGroupMember();
    }

    /**
     * 查询群组成员
     */
    private void queryGroupMember() {
        DialogCenter.showProgressDialog(PeerMainChildActivity.this, R.string.loading, false);
        RequestApi.getInstance().queryGroupMember(new RequestSubscriber<List<MemberInfo>>() {
            @Override
            public void failed(Throwable e) {
                DialogCenter.hideProgressDialog();
                CommonUtil.showToast(PeerMainChildActivity.this, e.getMessage());
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
        View root = View.inflate(PeerMainChildActivity.this, R.layout.dialog_custom_view_edt, null);
        EditText edtNickname = (EditText) root.findViewById(R.id.editText);
        edtNickname.setTypeface(HdApplication.typefaceGxa);
        edtNickname.setText(HdAppConfig.getNickname());
        Selection.setSelection(edtNickname.getText(), edtNickname.getText().length());
        DialogCenter.showDialog(PeerMainChildActivity.this, root, new DialogClickListener() {
            @Override
            public void p() {
                String nickname = edtNickname.getText().toString();
                nickname = nickname.replaceAll("(\r\n|\r|\n|\n\r)", "");
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtil.showToast(PeerMainChildActivity.this, R.string.tip_input_nickname);
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
        if (NetUtil.isConnected(PeerMainChildActivity.this)) {

            RequestApi.getInstance().modifyNickname(HdAppConfig.getDeviceNo(), nickname);
            DialogCenter.hideDialog();
            CommonUtil.showToast(PeerMainChildActivity.this, R.string
                    .modify_nickname_succeed);
            HdAppConfig.setNickname(nickname);
            ctvNickname.setText(nickname);
            queryGroupMember();


        } else {
            CommonUtil.showToast(PeerMainChildActivity.this, R.string.net_not_available);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cdelete_group:
                RequestApi.getInstance().existGroup(HdAppConfig.getDeviceNo(), PeerMainChildActivity.this);
                HdAppConfig.setAGroupNo(0);
                cdeleteGroup.setVisibility(View.INVISIBLE);
                finish();
                break;
            case R.id.civBack:
                finish();
                break;
            case R.id.civEditName:
                showModifyNicknameDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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