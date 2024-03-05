package com.PACOsoft.promise_betting.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;

public class Map_Help extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_help);
    }

    public void map_help_close(View view){
        finish();
    }
}
