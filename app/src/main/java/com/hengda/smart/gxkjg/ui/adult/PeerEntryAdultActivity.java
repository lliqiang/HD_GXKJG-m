package com.hengda.smart.gxkjg.ui.adult;

import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.IRequest;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.http.RequestSubscriber;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.group.GroupInfo;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

public class PeerEntryAdultActivity extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvCreateGroup)
    ImageView tvCreateGroup;
    @Bind(R.id.tvJoinGroup)
    ImageView tvJoinGroup;
    private Retrofit retrofit;
    private IRequest iRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_peer_entry);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(PeerEntryAdultActivity.this);
        tvTitle.setTypeface(HdApplication.typefaceGxa);
        ivBack.setOnClickListener(v -> finish());
        if (HdAppConfig.getLanguage().equals("ENGLISH")) {
            tvCreateGroup.setBackgroundResource(R.mipmap.ae_establish);
            tvJoinGroup.setBackgroundResource(R.mipmap.ae_joingroup);
        } else {
            tvCreateGroup.setBackgroundResource(R.mipmap.ac_establish);
            tvJoinGroup.setBackgroundResource(R.mipmap.ac_joingroup);
        }
        tvCreateGroup.setOnClickListener(v -> {
            if (NetUtil.isConnected(PeerEntryAdultActivity.this)) {
                createGroupDialog();
            } else {
                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
            }


        });
        tvJoinGroup.setOnClickListener(v -> showModifyNicknameDialog());
    }

    /*
     * 创建群组的弹框
     * */
    private void createGroupDialog() {
        View root = View.inflate(PeerEntryAdultActivity.this, R.layout.dialog_custom_view_edt, null);
        EditText edtgroup = (EditText) root.findViewById(R.id.editText);
        edtgroup.setTypeface(HdApplication.typefaceGxa);
        Selection.setSelection(edtgroup.getText(), edtgroup.getText().length());
        DialogCenter.showDialog(PeerEntryAdultActivity.this, root, new DialogClickListener() {
            @Override
            public void p() {
                String proupId = edtgroup.getText().toString();
                proupId = proupId.replaceAll("(\r\n|\r|\n|\n\r)", "");
                if (TextUtils.isEmpty(proupId)) {
                    CommonUtil.showToast(PeerEntryAdultActivity.this, getString(R.string.enter_group));
                } else {
                    Log.i("info", "-------deveiceNO:  " + HdAppConfig.getDeviceNo());
                    HdAppConfig.setGroupName(proupId);
                    HResourceUtil.showDownloadProgressDialog(PeerEntryAdultActivity.this, getString(R.string.load_verson));
                    RequestApi.getInstance().createGroup(new RequestSubscriber<GroupInfo>() {

                        @Override
                        public void failed(Throwable e) {
                            DialogCenter.hideProgressDialog();
                            HResourceUtil.hideDownloadProgressDialog();
                            Toast.makeText(PeerEntryAdultActivity.this, R.string.opreate, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void succeed(GroupInfo groupInfo) {
                            DialogCenter.hideProgressDialog();
                            HResourceUtil.hideDownloadProgressDialog();
                            HdAppConfig.setAGroupNo(Integer.parseInt(groupInfo.getGroup()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.showToast(PeerEntryAdultActivity.this, getString(R.string.group_create_succeed));
                                }
                            });


                            DialogCenter.showDialog(PeerEntryAdultActivity.this, new DialogClickListener() {


                                @Override
                                public void p() {
                                    DialogCenter.hideDialog();
                                    HdAppConfig.setAGroupNo(Integer.parseInt(groupInfo.getGroup()));
                                    HdAppConfig.setNickname(groupInfo.getUser_nickname());
                                    HdAppConfig.setJoinGroupTime(System.currentTimeMillis());
                                    openActivity(PeerEntryAdultActivity.this, PeerMainAdultActivity.class);
                                    finish();
                                }
                            }, new String[]{getString(R.string.warm_tip),
                                    getString(R.string.group_create_succeed) + groupInfo.getGroup(),
                                    getString(R.string.submit)});
                        }
                    }, HdAppConfig.getDeviceNo(), proupId);


                }
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
                finish();
            }
        }, new String[]{getString(R.string.group_no),
                getString(R.string.submit),
                getString(R.string.close)});
    }


    /**
     * 加入群组的弹框
     */
    private void showModifyNicknameDialog() {
        View root = View.inflate(PeerEntryAdultActivity.this, R.layout.dialog_custom_view_num, null);
        EditText edtgroup = (EditText) root.findViewById(R.id.editText);
        edtgroup.setTypeface(HdApplication.typefaceGxa);
        Selection.setSelection(edtgroup.getText(), edtgroup.getText().length());
        if (NetUtil.isConnected(PeerEntryAdultActivity.this)) {
            DialogCenter.showDialog(PeerEntryAdultActivity.this, root, new DialogClickListener() {
                @Override
                public void p() {
                    String proupId = edtgroup.getText().toString();
                    proupId = proupId.replaceAll("(\r\n|\r|\n|\n\r)", "");
                    if (TextUtils.isEmpty(proupId)) {
                        CommonUtil.showToast(PeerEntryAdultActivity.this, R.string.groupno);
                    } else {

                        Log.i("info", "-------deveiceNO:  " + HdAppConfig.getDeviceNo());
                        RequestApi.getInstance().joinGroup(HdAppConfig.getDeviceNo(), Integer.parseInt(proupId), PeerEntryAdultActivity.this);


                    }
                }

                @Override
                public void n() {
                    DialogCenter.hideDialog();
                    finish();
                }
            }, new String[]{getString(R.string.group_no),
                    getString(R.string.submit),
                    getString(R.string.close)});
        } else {
            CommonUtil.showToast(PeerEntryAdultActivity.this, R.string.net_not_available);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}