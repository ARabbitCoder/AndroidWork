package com.ayearn.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.ayearn.ui.R;
import com.ayearn.ui.customview.NumberProgressView;
import com.ayearn.ui.superview.SuperTextView;
import com.voole.utils.net.NetCallback;
import com.voole.utils.net.NetUtil;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-10 16:48
 */

public class ProgressDialog extends Dialog {
    private String downloadurl;
    private String downloadpath;
    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10001) {
                if(numberProgressView!=null){
                    numberProgressView.setProgress(msg.arg1);
                }
            }
        }
    };

    static NumberProgressView numberProgressView;
    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
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
        private String bdownurl = "";
        private String bdownloadPath="";
        private boolean flag = true;
        private String mPositiveButtonText, mNegativeButtonText;
        private OnClickListener mPositiveButtonListener, mNegativeButtonListener;
        private View layout = null;

        public Builder(Context context) {
            ProgressDialog.Builder.mContext = context;
            bdownloadPath = context.getFilesDir().getAbsolutePath();
        }

        public ProgressDialog.Builder setTitle(int titleId) {
            return this;
        }

        public ProgressDialog.Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public ProgressDialog.Builder setDownloadPath(String path) {
            this.bdownloadPath = path;
            return this;
        }

        public ProgressDialog.Builder setDownloadUrl(String url) {
            this.bdownurl = url;
            return this;
        }

        public ProgressDialog.Builder setPositiveButton(String text, final OnClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public ProgressDialog.Builder setNegativeButton(String text, final OnClickListener listener) {
            this.mNegativeButtonText = text;
            this.mNegativeButtonListener = listener;
            return this;
        }

        public ProgressDialog.Builder setCancelable(boolean flag) {
            this.flag = flag;
            return this;
        }

        public ProgressDialog create() {
            findLayout();
            return setDialog();
        }

        private void findLayout() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_progress_layout, null);
        }


        private ProgressDialog setDialog() {
            final ProgressDialog dialog = new ProgressDialog(mContext, R.style.alertDialog);
            TextView title = layout.findViewById(R.id.title);
            if (this.mTitle != null) {
                title.setText(this.mTitle);
                title.setVisibility(View.VISIBLE);
            } else {
                title.setVisibility(View.GONE);
            }
            numberProgressView = layout.findViewById(R.id.dialog_progress);
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
            dialog.setDownloadurl(bdownurl);
            dialog.setDownloadpath(bdownloadPath);
            return dialog;
        }
    }
    @Override
    public void show() {
        super.show();
        startDownload();
    }

    private void reDownload(){

    }

    private void startDownload(){
        if(TextUtils.isEmpty(downloadurl)){
            setTitle("下载路径为空");
        }
        NetUtil.getInstance().downloadFileWithProgress(downloadurl, downloadpath, "test.apk", true, new NetCallback() {
            @Override
            public void isUploading(int currentPercent) {

            }

            @Override
            public void isDownloading(int currentPercent) {
                Message msg = Message.obtain();
                msg.what = 10001;
                msg.arg1 = currentPercent;
                handler.sendMessage(msg);
            }

            @Override
            public void isFailed(int errorCode) {

            }

            @Override
            public void isSuccessed(String result) {

            }
        });
    }

    public void setDownloadurl(String downloadurl){
        this.downloadurl = downloadurl;
    }

    public void setDownloadpath(String path){
        this.downloadpath = path;
    }
}
