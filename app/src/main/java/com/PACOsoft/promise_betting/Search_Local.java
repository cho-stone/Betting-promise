package com.PACOsoft.promise_betting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Search_Local extends AppCompatActivity {
    private EditText search_word = findViewById(R.id.et_search_local);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_local);

        search_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Thread thread = new Make_Local_JSON_Thread();
                thread.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    //지우기
    public void btn_search_local_cancel(View v) {
        search_word.setText("");

    }
}