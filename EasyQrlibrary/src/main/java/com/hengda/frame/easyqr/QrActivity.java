package com.hengda.frame.easyqr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrActivity extends Activity implements QRCodeView.Delegate {
    private static final String TAG = QrActivity.class.getSimpleName();
    public static final String RESULT ="RESULT";
    public static final String NUMFILTER ="NUMFILTER";
    public static final int REQUST_QR=0;
    private QRCodeView  zxingView;
    private boolean justNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x51000000);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.qr);
        justNum=getIntent().getBooleanExtra(NUMFILTER,true);
        zxingView=(ZXingView)findViewById(R.id.zxingview);
        int cornerColor=getResourceId(this,"colorPrimary","color",getPackageName());
        zxingView.getScanBoxView().setCornerColor(cornerColor==0?Color.WHITE:getResources().getColor(cornerColor));
        zxingView.setDelegate(this);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getResourceId(Context context, String name, String type, String packageName){
        Resources themeResources=null;
        PackageManager pm=context.getPackageManager();
        try {
            themeResources=pm.getResourcesForApplication(packageName);
            return themeResources.getIdentifier(name, type, packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingView.startCamera();
        zxingView.showScanRect();
        zxingView.startSpot();
    }

    @Override
    protected void onStop() {
        super.onStop();
        zxingView.stopCamera();
    }
    @Override
    protected void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (!justNum){
            sendCallBackData(result);
            return;
        }
        boolean isNum = result.matches("[0-9]{0,8}");//数字+最大长度8
        if (isNum) {
            sendCallBackData(result);
        } else {
            Toast.makeText(this, "无效的编号", Toast.LENGTH_SHORT).show();
        }
        vibrate();
        zxingView.startSpot();
    }

    private void sendCallBackData(String result){
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(RESULT, result);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }



    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "未能正确打开相机", Toast.LENGTH_SHORT).show();
    }
}
