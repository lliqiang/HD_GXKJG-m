package com.hengda.smart.gxkjg.ui.web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengda.smart.common.util.AppUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.edit_info)
    TextView editInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        ivBack.setOnClickListener(view -> finish());

        if (HdAppConfig.getLanguage().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);
            editInfo.setTypeface(HdApplication.typefaceGxc);
        }
        titleTxt.setVisibility(View.INVISIBLE);
        String info = AppUtil.getVersionName(EditActivity.this);
        editInfo.setText(getString(R.string.current_version) + info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(titleTxt);
    }
}
