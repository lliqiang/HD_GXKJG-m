package com.hengda.smart.play;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hengda.smart.common.adapter.LCommonAdapter;
import com.hengda.smart.common.adapter.ViewHolder;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.entity.Content;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class CommentAc extends BaseActivity {


    @Bind(R.id.ivBack_comment)
    ImageView ivBackComment;
    @Bind(R.id.comment_list)
    ListView commentList;
    @Bind(R.id.activity_comment)
    LinearLayout activityComment;
    LCommonAdapter<Content> adapter;
    List<Content> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StatusBarCompat.translucentStatusBar(this);
        ButterKnife.bind(this);
        list= new ArrayList<>();
        Exhibit exhibit = (Exhibit) getIntent().getSerializableExtra("exhibit");
        if (NetUtil.isConnected(CommentAc.this)){

            RequestApi.getInstance().lookComment(new Subscriber<List<Content>>() {
                @Override
                public void onCompleted() {

//
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(CommentAc.this, R.string.no_comment, Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onNext(List<Content> content) {
                    list.addAll(content);
                    commentList.setAdapter(adapter = new LCommonAdapter<Content>(CommentAc.this,
                            R.layout.comment_item,list) {
                        @Override
                        public void convert(ViewHolder holder, Content dataBean) {
                            if (dataBean!=null) {
                                holder.setText(R.id.username_adult, dataBean.getUser_login());
                                holder.setText(R.id.content_adult, dataBean.getContent());
                            }
                            else {
                                Toast.makeText(CommentAc.this, "内容为空", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    adapter.notifyDataSetChanged();
                }
            },exhibit.getFileNo());
        }

        ivBackComment.setOnClickListener(v -> finish());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
