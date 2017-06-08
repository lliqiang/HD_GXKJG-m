package com.hengda.smart.gxkjg.ui.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.AcInfo;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QringAc extends AppCompatActivity implements View.OnLongClickListener {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.qring_img)
    ImageView qringImg;
    @Bind(R.id.qring)
    LinearLayout qring;
    private AcInfo acInfo;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qring);
        ButterKnife.bind(this);
        if (HdAppConfig.getUserType().equals("child")) {
            qring.setBackgroundResource(R.mipmap.bg_act_c_map);
        } else {
            qring.setBackgroundResource(R.mipmap.common);
        }
        acInfo = (AcInfo) getIntent().getSerializableExtra("AcInfo");
        path = acInfo.getQrimg();
        Glide.with(this).load(path).into(qringImg);

        ivBack.setOnClickListener(v -> finish());

        qringImg.setOnLongClickListener(this);

    }


    @Override
    public boolean onLongClick(View v) {
        qringImg.setDrawingCacheEnabled(true);//step 1
        Bitmap bitmap = qringImg.getDrawingCache();//step 2
        //step 3 转bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        DeCodeActivity.gotoActivity(QringAc.this, baos.toByteArray());//step 4
        qringImg.setDrawingCacheEnabled(false);//step 5
        finish();
        return true;

    }
}
