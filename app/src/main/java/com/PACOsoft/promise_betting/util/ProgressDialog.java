package com.PACOsoft.promise_betting.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import com.PACOsoft.promise_betting.R;

public class ProgressDialog extends Dialog { //커스텀 다이얼로그
    public ProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 다이얼로그 제목 안 보이게
        setContentView(R.layout.dialog_progress);
    }
}