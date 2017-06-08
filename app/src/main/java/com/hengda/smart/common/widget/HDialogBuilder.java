package com.hengda.smart.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.smart.common.dialog.BaseEffects;
import com.hengda.smart.common.util.ColorUtils;
import com.hengda.smart.gxkjg.R;


public class HDialogBuilder extends Dialog {

    private LinearLayout rootPanel;
    private LinearLayout topPanel;
    private LinearLayout customPanel;
    private View mDivider;
    private TextView mTitle;
    private TextView mMsg;
    private ImageView mIcon;
    private Button mBtnP;
    private Button mBtnN;
    private BaseEffects baseEffects;

    public HDialogBuilder(Context context) {
        super(context, R.style.hd_dialog_dim);
        init(context);
    }

    public HDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        View dialogContainer = View.inflate(context, R.layout.layout_hd_builder_dialog, null);
        rootPanel = (LinearLayout) dialogContainer.findViewById(R.id.rootPanel);
        topPanel = (LinearLayout) dialogContainer.findViewById(R.id.topPanel);
        customPanel = (LinearLayout) dialogContainer.findViewById(R.id.customPanel);
        mIcon = (ImageView) dialogContainer.findViewById(R.id.icon);
        mDivider = dialogContainer.findViewById(R.id.titleDivider);
        mTitle = (TextView) dialogContainer.findViewById(R.id.alertTitle);
        mMsg = (TextView) dialogContainer.findViewById(R.id.alertMsg);
        mBtnP = (Button) dialogContainer.findViewById(R.id.dialog_btn_p);
        mBtnN = (Button) dialogContainer.findViewById(R.id.dialog_btn_n);
        setContentView(dialogContainer);
        setOnShowListener(dialogInterface -> {
            if (baseEffects != null) {
                baseEffects.setDuration(500);
                baseEffects.start(rootPanel);
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public HDialogBuilder title(CharSequence title) {
        topPanel.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public HDialogBuilder title(int title) {
        topPanel.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param color
     * @return
     */
    public HDialogBuilder titleColor(int color) {
        mDivider.setVisibility(View.VISIBLE);
        mTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置Divider颜色
     *
     * @param color
     * @return
     */
    public HDialogBuilder dividerColor(int color) {
        mDivider.setVisibility(View.VISIBLE);
        mDivider.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public HDialogBuilder message(CharSequence msg) {
        mMsg.setVisibility(View.VISIBLE);
        mMsg.setText(msg);
        return this;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public HDialogBuilder message(int msg) {
        mMsg.setVisibility(View.VISIBLE);
        mMsg.setText(msg);
        return this;
    }

    /**
     * 设置消息文字颜色
     *
     * @param color
     * @return
     */
    public HDialogBuilder msgColor(int color) {
        mMsg.setTextColor(color);
        return this;
    }

    /**
     * 设置Dialog背景颜色
     *
     * @param color
     * @return
     */
    public HDialogBuilder dialogColor(int color) {
        if (color == Color.TRANSPARENT) {
            rootPanel.setBackgroundColor(color);
        } else {
            rootPanel.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        }
        return this;
    }

    /**
     * 设置Dialog背景
     *
     * @param bgResId
     * @return
     */
    public HDialogBuilder dialogBg(int bgResId) {
        rootPanel.setBackgroundResource(bgResId);
        return this;
    }

    /**
     * 设置Icon
     *
     * @param drawableResId
     * @return
     */
    public HDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    /**
     * 设置按钮背景（自定义XML）
     *
     * @param resId
     * @return
     */
    public HDialogBuilder btnBg(int resId) {
        mBtnP.setBackgroundResource(resId);
        mBtnN.setBackgroundResource(resId);
        return this;
    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public HDialogBuilder pBtnText(CharSequence text) {
        mBtnP.setVisibility(View.VISIBLE);
        mBtnP.setText(text);
        return this;
    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public HDialogBuilder pBtnText(int text) {
        mBtnP.setVisibility(View.VISIBLE);
        mBtnP.setText(text);
        return this;
    }

    /**
     * 取消按钮文字
     *
     * @param text
     * @return
     */
    public HDialogBuilder nBtnText(CharSequence text) {
        mBtnN.setVisibility(View.VISIBLE);
        mBtnN.setText(text);
        return this;
    }

    /**
     * 取消按钮文字
     *
     * @param text
     * @return
     */
    public HDialogBuilder nBtnText(int text) {
        mBtnN.setVisibility(View.VISIBLE);
        mBtnN.setText(text);
        return this;
    }

    /**
     * 确定按钮监听
     *
     * @param click
     * @return
     */
    public HDialogBuilder pBtnClickListener(View.OnClickListener click) {
        mBtnP.setOnClickListener(click);
        return this;
    }

    /**
     * 取消按钮监听
     *
     * @param click
     * @return
     */
    public HDialogBuilder nBtnClickListener(View.OnClickListener click) {
        mBtnN.setOnClickListener(click);
        return this;
    }

    /**
     * 设置Dlg出现动画
     *
     * @param baseEffects
     * @return
     */
    public HDialogBuilder baseEffects(BaseEffects baseEffects) {
        this.baseEffects = baseEffects;
        return this;
    }

    /**
     * 自定义Dialog主体部分
     *
     * @param view
     * @return
     */
    public HDialogBuilder setCustomView(View view) {
        if (customPanel.getChildCount() > 0) {
            customPanel.removeAllViews();
        }
        customPanel.addView(view);
        customPanel.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 自定义Dialog宽度
     *
     * @param width
     * @return
     */
    public HDialogBuilder setDlgWidth(int width) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }

    /**
     * 设置是否可以取消
     *
     * @param cancelable
     * @return
     */
    public HDialogBuilder cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * 设置是否可以点击周围取消
     *
     * @param outsideCancelable
     * @return
     */
    public HDialogBuilder outsideCancelable(boolean outsideCancelable) {
        setCanceledOnTouchOutside(outsideCancelable);
        return this;
    }

    /**
     * 设置字体
     *
     * @param typeface
     * @return
     */
    public HDialogBuilder setTypeface(Typeface typeface) {
        mTitle.setTypeface(typeface);
        mMsg.setTypeface(typeface);
        mBtnP.setTypeface(typeface);
        mBtnN.setTypeface(typeface);
        return this;
    }

}
