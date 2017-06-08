package com.hengda.smart.gxkjg.ui.child;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.hengda.smart.common.util.GlideCircleTransform;
import com.hengda.smart.common.util.KeyboardUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.common.mvp.m.ExhibitModel;
import com.hengda.smart.gxkjg.entity.Exhibition;
//import com.hengda.smart.gxkjg.ui.adult.ScanAdultActivity;
import com.hengda.smart.gxkjg.ui.adult.SearchAdultActivity;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.HtmlActivity;
import com.hengda.smart.play.ChildPlay;
import com.hengda.smart.play.Play;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchChildActivity extends BaseActivity {

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
    private Exhibit exhibit;
    private Intent intent;
    private String path;
    private InputMethodManager im;
    static final int READ_PHONE_STATE_REQUEST_CODE = 0x38;
    private String lastFileNO;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_search);
        ButterKnife.bind(this);
        View view = LayoutInflater.from(this).inflate(R.layout.head_layout, listView, false);
        ((TextView) view.findViewById(R.id.title_search)).setTypeface(HdApplication.typefaceGxc);
        listView.addHeaderView(view);
        StatusBarCompat.translucentStatusBar(SearchChildActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        im = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        ivBack.setOnClickListener(v -> {
            KeyboardUtil.closeKeyboard(edtInput, SearchChildActivity.this);
            finish();
        });
        ivClear.setOnClickListener(v -> edtInput.setText(""));
        if (checkSelfPermission(this, Manifest.permission.CAMERA)) {
            ivScan.setOnClickListener(v -> {
                KeyboardUtil.closeKeyboard(edtInput, SearchChildActivity.this);
//                openActivity(SearchChildActivity.this, ScanAdultActivity.class);
            });
        }
        //处理“不再提醒”，判断是否需要 向用户解释，为什么要申请该权限
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this).setMessage("需要申请照相机权限")
                    .setPositiveButton("申请", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SearchChildActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, READ_PHONE_STATE_REQUEST_CODE);
                        }
                    }).create().show();
        } else {
            //请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, READ_PHONE_STATE_REQUEST_CODE);
        }
        Acp.getInstance(SearchChildActivity.this).request(new AcpOptions.Builder()
                .setPermissions(Manifest.permission.CAMERA).setDeniedMessage("权限拒绝").setDeniedCloseBtn("关闭").build(), new AcpListener() {
            @Override
            public void onGranted() {
                ivScan.setOnClickListener(v -> {
                    //TODO


                    KeyboardUtil.closeKeyboard(edtInput, SearchChildActivity.this);
                    Intent i = new Intent(SearchChildActivity.this, QrActivity.class);
                    i.putExtra(QrActivity.NUMFILTER, false);
                    startActivityForResult(i, QrActivity.REQUST_QR);
                });
            }

            @Override
            public void onDenied(List<String> permissions) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtil.showToast(SearchChildActivity.this, "获取权限失败");
                    }
                });

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
        GlideCircleTransform glideCircleTransform = new GlideCircleTransform
                (SearchChildActivity.this);
        listView.setAdapter(adapter = new LCommonAdapter<Exhibit>(SearchChildActivity.this,
                R.layout.item_exhibit_digital_demand) {
            private TextView unitName;

            @Override
            public void convert(ViewHolder holder, Exhibit exhibit) {
                holder.setText(R.id.textView, exhibit.getName());
                unitName = holder.getView(R.id.textView2);
                unitName.setTextColor(getResources().getColor(R.color.unitchild));
                unitName.setTypeface(HdApplication.typefaceGxc);
                holder.setTypeface(HdApplication.typefaceGxc, R.id.textView);
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
                Glide.with(SearchChildActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.defult_s)
                        .error(R.mipmap.defult_s)
                        .into(imageView);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exhibit = adapter.getItem(position-1);

                if (SingleDown.isExhibitExist(exhibit.getFileNo())) {
                    intent = new Intent(SearchChildActivity.this, ChildPlay.class);
                    intent.putExtra("exhibit", exhibit);
                    RequestApi.getInstance().getCount(exhibit.getFileNo());
                    startActivity(intent);

                } else {

                    HResourceUtil.showDownloadDialog(SearchChildActivity.this);
                    HResourceUtil.loadSingleExist(SearchChildActivity.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            Toast.makeText(SearchChildActivity.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                            HResourceUtil.hideDownloadDialog();
                            Intent intent = new Intent(SearchChildActivity.this, ChildPlay.class);
                            intent.putExtra("exhibit", exhibit);
                            //做展品浏览次数上传
                            RequestApi.getInstance().getCount(exhibit.getFileNo());
                            startActivity(intent);
                        }

                        @Override
                        public void onLoadFailed() {
                            CommonUtil.showToast(SearchChildActivity.this, getString(R.string.down_fail));
                            HResourceUtil.hideDownloadDialog();
                        }
                    }, exhibit.getFileNo());

                }
            }
        });
    }

    private boolean checkSelfPermission(Context context, String permission) {
        //检查权限
        int permissionCheck = ActivityCompat.checkSelfPermission(context, permission);
        return PackageManager.PERMISSION_GRANTED == permissionCheck;
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtil.openKeyboard(edtInput, SearchChildActivity.this);
        loadAllExhibit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
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
            result = data.getStringExtra("RESULT");
        }
        if (data == null || resultCode != RESULT_OK) {
            CommonUtil.showToast(SearchChildActivity.this, getString(R.string.not_recognise));
        } else {
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
                                        intent = new Intent(SearchChildActivity.this, Play.class);
                                    } else {
                                        intent = new Intent(SearchChildActivity.this, ChildPlay.class);
                                    }
                                    intent.putExtra("exhibit", exhibit);
                                    RequestApi.getInstance().getCount(data.getStringExtra("RESULT"));
                                    startActivity(intent);
                                } else {
                                    // TODO: 2017/4/6
                                    HResourceUtil.showDownloadDialog(SearchChildActivity.this);
                                    HResourceUtil.loadSingleExist(SearchChildActivity.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            Toast.makeText(SearchChildActivity.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                                            HResourceUtil.hideDownloadDialog();
                                            if (HdAppConfig.getUserType().equals("child")) {
                                                intent = new Intent(SearchChildActivity.this, ChildPlay.class);
                                            } else {
                                                intent = new Intent(SearchChildActivity.this, Play.class);
                                            }

                                            intent.putExtra("exhibit", exhibit);
                                            //做展品浏览次数上传
                                            RequestApi.getInstance().getCount(exhibit.getFileNo());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            CommonUtil.showToast(SearchChildActivity.this, getString(R.string.down_fail));
                                            HResourceUtil.hideDownloadDialog();
                                        }
                                    }, exhibit.getFileNo());

                                }

                            }
                            //做展品浏览次数上传

                        } else {
                            CommonUtil.showToast(SearchChildActivity.this, getString(R.string.not_recognise));
                        }
                    }
                });

            } else if (isHtml) {
                Intent intent = new Intent(SearchChildActivity.this, HtmlActivity.class);
                intent.putExtra("html", result);
                startActivity(intent);

            } else {
                CommonUtil.showToast(SearchChildActivity.this, getString(R.string.not_recognise));
            }

        }

    }

}
