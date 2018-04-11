package com.ayearn.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ayearn.ui.R;
import com.ayearn.ui.superview.SuperTextView;
import com.voole.utils.log.LogUtil;

/**
 * @author liujingwei
 * @desc 自定义对话框
 * @time 2018-1-11 17:53
 */

public class AlertAppDiaLog extends Dialog {

    public AlertAppDiaLog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AlertAppDiaLog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setWindowAnimations(R.style.bottom_menu_animation);
        //设置出现位置
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        int a = dm.widthPixels/dm.heightPixels;
        int width = 380;
        int height = 200;
        if(a>0) {
            width = (int)(dm.widthPixels*0.4);
            height = width*2/3;
        }else {
            width = (int)(dm.widthPixels*0.6);
            height = width*2/3;
        }
        layoutParams.width = width;
        layoutParams.height = height;
        window.setAttributes(layoutParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void init(Context context) {

    }

    public static class Builder {
        private static Context mContext;
        private CharSequence mTitle = "";
        private String mMsg = "";
        private boolean flag = true;
        private boolean showScroller = false;
        private String mPositiveButtonText, mNegativeButtonText;
        private OnClickListener mPositiveButtonListener, mNegativeButtonListener;
        private View layout = null;

        public Builder(Context context) {
            Builder.mContext = context;
        }

        public Builder setTitle(int titleId) {
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String msg) {
            this.mMsg = msg;
            return this;
        }

        public Builder setPositiveButton(String text, final OnClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, final OnClickListener listener) {
            this.mNegativeButtonText = text;
            this.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean flag) {
            this.flag = flag;
            return this;
        }

        public AlertAppDiaLog create() {
            findLayout();
            return setDialog();
        }

        private void findLayout() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_layout, null);
        }


        private AlertAppDiaLog setDialog() {
            final AlertAppDiaLog dialog = new AlertAppDiaLog(mContext, R.style.alertDialog);
            TextView title = layout.findViewById(R.id.title);
            if (this.mTitle != null) {
                title.setText(this.mTitle);
                title.setVisibility(View.VISIBLE);
            } else {
                title.setVisibility(View.GONE);
            }
            TextView msg = layout.findViewById(R.id.message);
            msg.setText(mMsg);
            if (showScroller) {
                msg.setMovementMethod(ScrollingMovementMethod.getInstance());
                msg.setScrollbarFadingEnabled(false);
                msg.setFocusable(true);
            } else {
                msg.setFocusable(true);
            }
            if (!TextUtils.isEmpty(mPositiveButtonText)) {
                final SuperTextView positiveBtn = layout.findViewById(R.id.yes);
                positiveBtn.setVisibility(View.VISIBLE);
                positiveBtn.setText(mPositiveButtonText);

                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (mPositiveButtonListener != null) {
                            mPositiveButtonListener.onClick(dialog, BUTTON_POSITIVE);
                        }
                    }
                });
            }
            if (!TextUtils.isEmpty(mNegativeButtonText)) {
                final SuperTextView positiveBtn = layout.findViewById(R.id.no);
                positiveBtn.setVisibility(View.VISIBLE);
                positiveBtn.setText(mNegativeButtonText);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (mNegativeButtonListener != null) {
                            mNegativeButtonListener.onClick(dialog, BUTTON_NEGATIVE);
                        }
                    }
                });
            }
            dialog.setContentView(layout);
            dialog.setCancelable(flag);
            return dialog;
        }
    }

}
