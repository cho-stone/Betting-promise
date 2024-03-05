package com.PACOsoft.promise_betting.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;

public class Create_Room_Help extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_help);
    }

    public void create_room_help_close(View view){
        finish();
    }
}

