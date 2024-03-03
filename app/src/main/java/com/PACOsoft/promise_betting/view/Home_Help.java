package com.PACOsoft.promise_betting.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;

public class Home_Help extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_help);
    }

    public void home_help_close(View view){
        finish();
    }
}
