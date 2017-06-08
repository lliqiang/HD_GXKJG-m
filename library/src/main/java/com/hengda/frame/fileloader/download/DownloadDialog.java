package com.hengda.frame.fileloader.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.frame.fileloader.R;

import java.text.DecimalFormat;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/11/10.
 */

public class DownloadDialog extends MaterialDialog {
    private Context mContext;
    private TextView title;
    private TextView size;
    private Button cancle;
    private NumberProgressBar mProgressBar;

    public DownloadDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.fileloader_download_dialog, null);
        mProgressBar = (NumberProgressBar) layout.findViewById(R.id.progress);
        title = (TextView) layout.findViewById(R.id.title);
        size = (TextView) layout.findViewById(R.id.size);
        cancle = (Button) layout.findViewById(R.id.cancle);
        setView(layout);
    }

    public DownloadDialog setTitle(String msg) {
        title.setText(msg);
        return this;
    }

    public void process(int soFarBytes, int totalBytes) {
        mProgressBar.setMax(new Long(totalBytes).intValue() / 100);
        mProgressBar.setProgress(new Long(soFarBytes).intValue() / 100);
    }

    public void process(int process) {
        mProgressBar.setMax(100);
        mProgressBar.setProgress(process);
    }

    public void setSize(int totleSize) {
        double result = (double) (totleSize / 1024) / 1024;
        DecimalFormat df = new DecimalFormat("###0.00");
        size.setText(df.format(result) + "M");
    }

    public DownloadDialog showCancleButton(boolean flag) {
        if (flag)
            cancle.setVisibility(View.VISIBLE);
        else
            cancle.setVisibility(View.GONE);
        return this;
    }

    public DownloadDialog cancelListener(View.OnClickListener click) {
        cancle.setOnClickListener(click);
        return this;
    }

    public DownloadDialog outsideCancelable(boolean outsideCancelable) {
        setCanceledOnTouchOutside(outsideCancelable);
        return this;
    }


}
