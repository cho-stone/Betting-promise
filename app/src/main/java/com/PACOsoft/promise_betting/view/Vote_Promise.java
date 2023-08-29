package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

import com.PACOsoft.promise_betting.R;

public class Vote_Promise extends Dialog {

    public Vote_Promise(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.activity_vote_promise);


    }
}