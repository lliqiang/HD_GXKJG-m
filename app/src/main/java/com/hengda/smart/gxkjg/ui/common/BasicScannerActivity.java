package com.hengda.smart.gxkjg.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.common.Intents;
//import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
//import com.mylhyl.zxing.scanner.common.Intents;


/**
 * Created by hupei on 2016/7/7.
 */
public abstract class BasicScannerActivity extends AppCompatActivity implements OnScannerCompletionListener {
    public static final int REQUEST_CODE_SCANNER = 188;
    public static final String EXTRA_RETURN_SCANNER_RESULT = "return_scanner_result";
    private static final String TAG = "BasicScannerActivity";

    private boolean mReturnScanResult;

    /**
     * 子类实现，根据 ParsedResultType 处理业务
     *
     * @param result
     * @param type
     * @param bundle
     */
    abstract void onResultActivity(Result result, ParsedResultType type, Bundle bundle);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReturnScanResult = extras.getBoolean(EXTRA_RETURN_SCANNER_RESULT);
        }
    }

    @Override
    public void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        if (rawResult == null) {
            Toast.makeText(this, "未发现二维码", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (mReturnScanResult) {
            onReturnScanResult(rawResult);
            return;
        }
        Bundle bundle = new Bundle();
        ParsedResultType type = parsedResult.getType();
        Log.i(TAG, "ParsedResultType: " + type);
        switch (type) {



            case URI:
                URIParsedResult uri = (URIParsedResult) parsedResult;
                Log.i(TAG, "uri: " + uri.getURI());
                bundle.putSerializable(Intents.Scan.RESULT, new URIResult(uri));
                break;
            case TEXT:
                TextParsedResult textParsedResult = (TextParsedResult) parsedResult;
                bundle.putString(Intents.Scan.RESULT, textParsedResult.getText());
                break;

        }
        onResultActivity(rawResult, type, bundle);
    }

    private void onReturnScanResult(Result rawResult) {
        Intent intent = getIntent();
        intent.putExtra(Intents.Scan.RESULT, rawResult.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
