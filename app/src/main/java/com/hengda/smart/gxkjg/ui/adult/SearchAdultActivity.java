package com.hengda.smart.gxkjg.ui.adult;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rxpermisson.PermissionAppCompatActivity;
import com.google.zxing.client.result.ParsedResultType;
import com.hengda.frame.easyqr.QrActivity;
import com.hengda.smart.common.adapter.LCommonAdapter;
import com.hengda.smart.common.adapter.ViewHolder;
import com.hengda.smart.common.dbase.HBriteDatabase;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.permission.Acp;
import com.hengda.smart.common.permission.AcpListener;
import com.hengda.smart.common.permission.AcpOptions;
import com.hengda.smart.common.rxbus.RxBusUtil;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.KeyboardUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.common.mvp.m.ExhibitModel;
import com.hengda.smart.gxkjg.entity.Exhibition;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.HtmlActivity;
import com.hengda.smart.play.ChildPlay;
import com.hengda.smart.play.Play;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.hengda.smart.gxkjg.R.id.scannerView;

public class SearchAdultActivity extends PermissionAppCompatActivity {
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.edtInput)
    EditText edtInput;
    @Bind(R.id.ivClear)
    ImageView ivClear;
    @Bind(R.id.ivScan)
    ImageView ivScan;
    @Bind(R.id.listView)
    ListView listView;

    Subscription subscription;
    LCommonAdapter<Exhibit> adapter;
    String path;
    private Exhibit exhibit;
    private Intent intent;
    private InputMethodManager im;
    private String lastFileNO;
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_search);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(SearchAdultActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        im = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        ivBack.setOnClickListener(v -> {
            KeyboardUtil.closeKeyboard(edtInput, SearchAdultActivity.this);
            finish();
        });

        View view1 = LayoutInflater.from(this).inflate(R.layout.head_layout, listView, false);
        ((TextView) view1.findViewById(R.id.title_search)).setTypeface(HdApplication.typefaceGxa);
        listView.addHeaderView(view1);
        ivClear.setOnClickListener(v -> edtInput.setText(""));
        ivScan.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        if (cameraIsCanUse()) {
                            KeyboardUtil.closeKeyboard(edtInput, SearchAdultActivity.this);
                            Intent i = new Intent(SearchAdultActivity.this, QrActivity.class);
                            i.putExtra(QrActivity.NUMFILTER, false);
                            startActivityForResult(i, QrActivity.REQUST_QR);

                        } else {
                            Toast.makeText(SearchAdultActivity.this, "请在设置中打开照相机权限", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions) {

                        Toast.makeText(SearchAdultActivity.this, "请在设置中打开照相机权限", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (cameraIsCanUse()) {

                    KeyboardUtil.closeKeyboard(edtInput, SearchAdultActivity.this);
                    Intent i = new Intent(SearchAdultActivity.this, QrActivity.class);
                    i.putExtra(QrActivity.NUMFILTER, false);
                    startActivityForResult(i, QrActivity.REQUST_QR);

                } else {
                    Toast.makeText(this, "请在设置中打开照相机权限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RxTextView.textChangeEvents(edtInput)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String changedMessage = textViewTextChangeEvent.text().toString();
                        if (TextUtils.isEmpty(changedMessage)) {
                            ivClear.setVisibility(View.GONE);
                            loadAllExhibit();
                        } else {
                            ivClear.setVisibility(View.VISIBLE);
                            loadExhibitByInput();
                        }
                    }
                });

        listView.setAdapter(adapter = new LCommonAdapter<Exhibit>(SearchAdultActivity.this,
                R.layout.item_exhibit_digital_demand) {
            private TextView unitName;

            @Override
            public void convert(ViewHolder holder, Exhibit exhibit) {
                holder.setText(R.id.textView, exhibit.getName());
                unitName = ((TextView) holder.getView(R.id.textView2));
                unitName.setTextColor(getResources().getColor(R.color.unitadut));
                unitName.setTypeface(HdApplication.typefaceGxa);
                holder.setTypeface(HdApplication.typefaceGxa, R.id.textView);
                ImageView imageView = holder.getView(R.id.imageView);
                StringBuilder sb = new StringBuilder();
                String table = HdAppConfig.getLanguage() + "_Unit";
                sb.append("SELECT * FROM ").append(table);
                HBriteDatabase.getInstance()
                        .getDb()
                        .createQuery(table, sb.toString(), new String[]{})
                        .mapToList(cursor -> Exhibition.getExhibitInfo(cursor))
                        .flatMap(exhibitions -> Observable.from(exhibitions))
                        .filter(new Func1<Exhibition, Boolean>() {
                            @Override
                            public Boolean call(Exhibition exhibition) {
                                return exhibit.getFloor() == exhibit.getFloor() && exhibit.getUnitNo() == exhibition.getUnitNo();
                            }
                        })
                        .subscribe(exhibition -> {
                            unitName.setText(exhibition.getName());
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        });

                path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + "_icon.png";
                Glide.with(SearchAdultActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.defult_s)
                        .error(R.mipmap.defult_s)
                        .into(imageView);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exhibit = adapter.getItem(position-1);

                if (SingleDown.isExhibitExist(exhibit.getFileNo())) {

                    intent = new Intent(SearchAdultActivity.this, Play.class);
                    intent.putExtra("exhibit", exhibit);
                    if (NetUtil.isConnected(SearchAdultActivity.this)) {

                        RequestApi.getInstance().getCount(exhibit.getFileNo());
                    }
                    startActivity(intent);

                } else {

                    HResourceUtil.showDownloadDialog(SearchAdultActivity.this);
                    HResourceUtil.loadSingleExist(SearchAdultActivity.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            Toast.makeText(SearchAdultActivity.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                            HResourceUtil.hideDownloadDialog();
                            Intent intent = new Intent(SearchAdultActivity.this, Play.class);
                            intent.putExtra("exhibit", exhibit);
                            //做展品浏览次数上传
                            if (NetUtil.isConnected(SearchAdultActivity.this)) {

                                RequestApi.getInstance().getCount(exhibit.getFileNo());
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onLoadFailed() {
                            CommonUtil.showToast(SearchAdultActivity.this, getString(R.string.down_fail));
                            HResourceUtil.hideDownloadDialog();
                        }
                    }, exhibit.getFileNo());


                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL |
                        scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    if (im.isActive()) {
                        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        KeyboardUtil.openKeyboard(edtInput, SearchAdultActivity.this);
        loadAllExhibit();
    }

    protected void openActivity(Context context, Class<?> pClass) {
        openActivity(context, pClass, null, null);
    }

    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle, String action) {
        Intent intent = new Intent(context, pClass);
        if (action != null) {
            intent.setAction(action);
        }
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
        ButterKnife.unbind(this);
    }

    /**
     * 加载所有展品
     */
    private void loadAllExhibit() {
        String table = HdAppConfig.getLanguage();
        String sql = "SELECT * FROM " + table;
        doSubscribe(table, sql);
    }

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }


    /**
     * 根据输入加载展品
     */
    private void loadExhibitByInput() {
        String table = HdAppConfig.getLanguage();
        String sql = new StringBuilder()
                .append("SELECT * FROM ")
                .append(table)
                .append(" WHERE Name LIKE '%")
                .append(edtInput.getText())
                .append("%' OR FileNo LIKE '%")
                .append(edtInput.getText())
                .append("%'").toString();
        doSubscribe(table, sql);
    }


    /**
     * adapter订阅查询操作
     *
     * @param table
     * @param sql
     */
    private void doSubscribe(String table, String sql) {
        unsubscribe();
        subscription = HBriteDatabase.getInstance()
                .getDb()
                .createQuery(table, sql, new String[]{})
                .mapToList(ExhibitModel::cursorToExhibit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);
    }

    /**
     * 取消订阅，避免内存泄漏
     */
    private void unsubscribe() {
        RxBusUtil.unsubscribe(subscription);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.i("data", "-----------data: " + data.getStringExtra("RESULT"));

        }
        if (data == null || resultCode != RESULT_OK) {
            CommonUtil.showToast(SearchAdultActivity.this, getString(R.string.not_recognise));
        } else {
            result = data.getStringExtra("RESULT");
            boolean isHtml = data.getStringExtra("RESULT").contains("http");
            boolean isNum = data.getStringExtra("RESULT").matches("[0-9]+");
            if (isNum) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor = HResDdUtil.getInstance().QueryExhibit(data.getStringExtra("RESULT").toString());

                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                exhibit = Exhibit.getExhibitInfo(cursor);
                            }
                            cursor.close();
                            if (exhibit != null) {
                                //做资源是否存在的判断
                                if (SingleDown.isExhibitExist(data.getStringExtra("RESULT").toString())) {

                                    if (HdAppConfig.getUserType().equals("adult")) {
                                        intent = new Intent(SearchAdultActivity.this, Play.class);
                                    } else {
                                        intent = new Intent(SearchAdultActivity.this, ChildPlay.class);
                                    }


                                    intent.putExtra("exhibit", exhibit);
                                    RequestApi.getInstance().getCount(data.getStringExtra("RESULT"));
                                    startActivity(intent);
                                } else {
                                    // TODO: 2017/4/6
                                    HResourceUtil.showDownloadDialog(SearchAdultActivity.this);
                                    HResourceUtil.loadSingleExist(SearchAdultActivity.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            Toast.makeText(SearchAdultActivity.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                                            HResourceUtil.hideDownloadDialog();
                                            if (HdAppConfig.getUserType().equals("child")) {
                                                intent = new Intent(SearchAdultActivity.this, ChildPlay.class);
                                            } else {
                                                intent = new Intent(SearchAdultActivity.this, Play.class);
                                            }

                                            intent.putExtra("exhibit", exhibit);
                                            //做展品浏览次数上传
                                            RequestApi.getInstance().getCount(exhibit.getFileNo());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            CommonUtil.showToast(SearchAdultActivity.this, getString(R.string.down_fail));
                                            HResourceUtil.hideDownloadDialog();
                                        }
                                    }, exhibit.getFileNo());

                                }

                            }
                            //做展品浏览次数上传

                        } else {
                            CommonUtil.showToast(SearchAdultActivity.this, getString(R.string.not_recognise));
                        }
                    }
                });

            } else if (isHtml) {
                Intent intent = new Intent(SearchAdultActivity.this, HtmlActivity.class);
                intent.putExtra("html", result);
                startActivity(intent);

            } else {
                CommonUtil.showToast(SearchAdultActivity.this, getString(R.string.not_recognise));
            }


        }


        if (data != null && resultCode == RESULT_OK) {
            Log.i("1111", "onActivityResult: " + data.getStringExtra(QrActivity.RESULT));
            ;
        }
    }


}
