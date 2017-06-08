package com.hengda.smart.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hengda.smart.common.dialog.BaseEffects;
import com.hengda.smart.gxkjg.R;


public class TipDialog extends Dialog {


    private BaseEffects baseEffects;

    private TextView learnMore;
    private Button sureDialog;


    public TipDialog(Context context) {
        super(context,R.style.hd_dialog_dim);
        init(context);
    }

    public TipDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        View dialogContainer = View.inflate(context, R.layout.tip_layout, null);

        learnMore= (TextView) dialogContainer.findViewById(R.id.learnmore);
        learnMore.setSelected(true);
        sureDialog= (Button) dialogContainer.findViewById(R.id.iknow);


        setContentView(dialogContainer);
        setOnShowListener(dialogInterface -> {
            if (baseEffects != null) {
                baseEffects.setDuration(500);

            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
//    public ListDialog title(CharSequence title) {
//        topPanel.setVisibility(View.VISIBLE);
//        mTitle.setText(title);
//        return this;
//    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
//    public ListDialog title(int title) {
//        topPanel.setVisibility(View.VISIBLE);
//        mTitle.setText(title);
//        return this;
//    }

    /**
     * 设置标题文字颜色
     *
     * @param color
     * @return
     */
//    public ListDialog titleColor(int color) {
//        mDivider.setVisibility(View.VISIBLE);
//        mTitle.setTextColor(color);
//        return this;
//    }

    /**
     * 设置Divider颜色
     *
     * @param color
     * @return
     */
//    public ListDialog dividerColor(int color) {
//        mDivider.setVisibility(View.VISIBLE);
//        mDivider.setBackgroundColor(color);
//        return this;
//    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public TipDialog message(CharSequence msg) {
        learnMore.setVisibility(View.VISIBLE);
        learnMore.setText(msg);
        return this;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public TipDialog message(int msg) {
        learnMore.setVisibility(View.VISIBLE);
        learnMore.setText(msg);
        return this;
    }

    /**
     * 设置消息文字颜色
     *
     * @param color
     * @return
     */
//    public ListDialog msgColor(int color) {
//        swipFloor.setTextColor(color);
//        return this;
//    }

    /**
     * 设置Dialog背景颜色
     *
     * @param color
     * @return
     */
//    public ListDialog dialogColor(int color) {
//        if (color == Color.TRANSPARENT) {
//            rootPanel.setBackgroundColor(color);
//        } else {
//            rootPanel.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
//        }
//        return this;
//    }

    /**
     * 设置Dialog背景
     *
     * @param bgResId
     * @return
     */
//    public ListDialog dialogBg(int bgResId) {
//        rootPanel.setBackgroundResource(bgResId);
//        return this;
//    }
//
//    /**
//     * 设置Icon
//     *
//     * @param drawableResId
//     * @return
//     */
//    public ListDialog withIcon(int drawableResId) {
//        mIcon.setImageResource(drawableResId);
//        return this;
//    }

//    /**
//     * 设置按钮背景（自定义XML）
//     *
//     * @param resId
//     * @return
//     */
//    public ListDialog btnBg(int resId) {
//        mBtnP.setBackgroundResource(resId);
//        mBtnN.setBackgroundResource(resId);
//        return this;
//    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public TipDialog pBtnText(CharSequence text) {
        sureDialog.setVisibility(View.VISIBLE);
        sureDialog.setText(text);
        return this;
    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public TipDialog pBtnText(int text) {
        sureDialog.setVisibility(View.VISIBLE);
        sureDialog.setText(text);
        return this;
    }




    /**
     * 确定按钮监听
     *
     * @param click
     * @return
     */
    public TipDialog pBtnClickListener(View.OnClickListener click) {
        sureDialog.setOnClickListener(click);
        return this;
    }



    /**
     * 设置Dlg出现动画
     *
//     * @param baseEffects
     * @return
     */
    public TipDialog baseEffects(BaseEffects baseEffects) {
        this.baseEffects = baseEffects;
        return this;
    }



    /**
     * 设置是否可以取消
     *
     * @param cancelable
     * @return
     */
    public TipDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * 设置是否可以点击周围取消
     *
     * @param outsideCancelable
     * @return
     */
    public TipDialog outsideCancelable(boolean outsideCancelable) {
        setCanceledOnTouchOutside(outsideCancelable);
        return this;
    }

    /**
     * 设置字体
     *
     * @param typeface
     * @return
     */
    public TipDialog setTypeface(Typeface typeface) {
        learnMore.setTypeface(typeface);
        return this;
    }

}
