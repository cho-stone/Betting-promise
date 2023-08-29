package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.PACOsoft.promise_betting.R;

public class Vote_Promise extends Dialog {
    private Button btn_for, btn_against;

    public Vote_Promise(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_vote_promise);

        btn_for = findViewById(R.id.id_vote_for);
        btn_against = findViewById(R.id.id_vote_against);

        btn_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_against.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    //바깥영역 터치방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return false;
    }

    //뒤로가기 비활성
    @Override
    public void onBackPressed() {
        return;
    }
}