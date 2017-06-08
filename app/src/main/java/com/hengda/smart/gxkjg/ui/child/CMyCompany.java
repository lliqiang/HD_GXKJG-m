package com.hengda.smart.gxkjg.ui.child;

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

public class CMyCompany extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.company_txt)
    TextView companyTxt;
    @Bind(R.id.create_group)
    ImageView createGroup;
    @Bind(R.id.add_group)
    ImageView addGroup;
//    private GroupInfo.DataBean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmy_company);
        StatusBarCompat.translucentStatusBar(this);
        ButterKnife.bind(this);
        companyTxt.setTypeface(HdApplication.typefaceGxc);
        ivBack.setOnClickListener(v -> finish());
        createGroup.setOnClickListener(v ->{
            if (NetUtil.isConnected(CMyCompany.this)){
                createGroupDialog();
            }else {
                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
            }
        });
        addGroup.setOnClickListener(v ->  showModifyNicknameDialog());
        if (HdAppConfig.getLanguage().equals("ENGLISH")){
            createGroup.setBackgroundResource(R.mipmap.ce_establish);
            addGroup.setBackgroundResource(R.mipmap.ce_joingroup);
        }else if (HdAppConfig.getLanguage().equals("CHINESE")){
            createGroup.setBackgroundResource(R.mipmap.ccestablish);
            addGroup.setBackgroundResource(R.mipmap.cc_joingroup);
        }


}


    /*
    * 创建群组的弹框
    * */
    private void createGroupDialog(){
        View root = View.inflate(CMyCompany.this, R.layout.dialog_custom_view_edt, null);
        EditText edtgroup = (EditText) root.findViewById(R.id.editText);
        edtgroup.setTypeface(HdApplication.typefaceGxa);
        Selection.setSelection(edtgroup.getText(), edtgroup.getText().length());
        DialogCenter.showDialog(CMyCompany.this, root, new DialogClickListener() {
            @Override
            public void p() {
                String proupId = edtgroup.getText().toString();
                proupId = proupId.replaceAll("(\r\n|\r|\n|\n\r)", "");
                if (TextUtils.isEmpty(proupId)) {
                    CommonUtil.showToast(CMyCompany.this,"请输入群组名称：");
                } else {
                    Log.i("info","-------deveiceNO:  "+HdAppConfig.getDeviceNo());
                    HdAppConfig.setGroupName(proupId);

                    RequestApi.getInstance().createGroup(new RequestSubscriber<GroupInfo>() {

                        @Override
                        public void failed(Throwable e) {
                            DialogCenter.hideProgressDialog();
                            Toast.makeText(CMyCompany.this, getString(R.string.opreate), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void succeed(GroupInfo groupInfo) {
                            DialogCenter.hideProgressDialog();
                            HdAppConfig.setAGroupNo(Integer.parseInt(groupInfo.getGroup()));
                            HdAppConfig.setNickname(groupInfo.getUser_nickname());
                            DialogCenter.showDialog(CMyCompany.this, new DialogClickListener() {


                                @Override
                                public void p() {
                                    DialogCenter.hideDialog();
                                    HdAppConfig.setJoinGroupTime(System.currentTimeMillis());

                                    openActivity(CMyCompany.this, PeerMainChildActivity.class);
                                }
                            }, new String[]{getString(R.string.warm_tip),
                                    getString(R.string.group_create_succeed) + groupInfo.getGroup(),
                                    getString(R.string.submit)});
                        }
                    }, HdAppConfig.getDeviceNo(),proupId);



                }
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
            }
        }, new String[]{getString(R.string.group_no),
                getString(R.string.submit),
                getString(R.string.close)});
    }




    /**
     * 加入群组的弹框
     */
    private void showModifyNicknameDialog() {
        View root = View.inflate(CMyCompany.this, R.layout.dialog_custom_view_num, null);
        EditText edtgroup = (EditText) root.findViewById(R.id.editText);
        edtgroup.setTypeface(HdApplication.typefaceGxa);
        Selection.setSelection(edtgroup.getText(), edtgroup.getText().length());
        if (NetUtil.isConnected(CMyCompany.this)) {
            DialogCenter.showDialog(CMyCompany.this, root, new DialogClickListener() {
                @Override
                public void p() {
                    String proupId = edtgroup.getText().toString();
                    proupId = proupId.replaceAll("(\r\n|\r|\n|\n\r)", "");
                    if (TextUtils.isEmpty(proupId)) {
                        CommonUtil.showToast(CMyCompany.this, R.string.groupno);
                    } else {
                        Log.i("info", "-------deveiceNO:  " + HdAppConfig.getDeviceNo());
                        HdAppConfig.setCGroupName(getString(R.string.group_no)+proupId);
                        RequestApi.getInstance().joinGroup(HdAppConfig.getDeviceNo(), Integer.parseInt(proupId), CMyCompany.this);

                    }
                }

                @Override
                public void n() {
                    DialogCenter.hideDialog();
                }
            }, new String[]{getString(R.string.group_no),
                    getString(R.string.submit),
                    getString(R.string.close)});
        }else {
            CommonUtil.showToast(CMyCompany.this, R.string.net_not_available);
        }
    }
    /**
     * 创建组群
     */
    private void createGroup() {
        if (NetUtil.isConnected(CMyCompany.this)) {
            DialogCenter.showProgressDialog(CMyCompany.this, R.string.waiting, false);
            RequestApi.getInstance().createGroup(new RequestSubscriber<GroupInfo>() {

                @Override
                public void failed(Throwable e) {
                    DialogCenter.hideProgressDialog();
                }

                @Override
                public void succeed(GroupInfo groupInfo) {
                    DialogCenter.hideProgressDialog();
                    DialogCenter.showDialog(CMyCompany.this, new DialogClickListener() {


                        @Override
                        public void p() {
                            DialogCenter.hideDialog();
                            HdAppConfig.setAGroupNo(Integer.parseInt(groupInfo.getGroup()));
                            HdAppConfig.setNickname(groupInfo.getUser_nickname());
                            HdAppConfig.setJoinGroupTime(System.currentTimeMillis());
                            openActivity(CMyCompany.this, PeerMainChildActivity.class);
                        }
                    }, new String[]{getString(R.string.warm_tip),
                            getString(R.string.group_create_succeed) + groupInfo.getGroup(),
                            getString(R.string.submit)});
                }
            }, HdAppConfig.getDeviceNo(),"");
        } else {
            CommonUtil.showToast(CMyCompany.this, R.string.net_not_available);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
