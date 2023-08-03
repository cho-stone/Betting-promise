package com.PACOsoft.promise_betting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class Create_Room extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    TextView textView;
    TextView timeText;
    private int i_year;
    private int i_month;
    private int i_day;
    private int i_hour;
    private int i_min;
    private String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        Intent intent = getIntent();
        myId = intent.getStringExtra("myId"); //Home에서 intent해준 id를 받아옴
    }

    //날짜데이터 받아오기
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "/" + month_string + "/" + day_string);

        textView = findViewById(R.id.date_Tview);
        textView.setText(dateMessage);
        i_year = year;
        i_month = month+1;
        i_day = day;
    }

    //날짜 선택 버튼
    public void btn_date_set(View view){
        DialogFragment newFragment = new Date_Picker();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    //시간 선택 버튼
    public void btn_time_set(View view){
        DialogFragment timepicker = new Time_Picker();
        timepicker.show(getSupportFragmentManager(), "time picker");
    }

    //위치 선택 버튼
    public void intent_btn_search_local(View view){
        Intent intent = new Intent(this, Search_Local.class);
        startActivity(intent);
    }

    //친구 초대 버튼
    public void btn_intent_invite_friend(View view){
        Intent intent = new Intent(this, Invite_Friend.class);
        int date = 1;
        int time = 2;
        int position =3;
        intent.putExtra("year", getYear());
        intent.putExtra("month", getMonth());
        intent.putExtra("day", getDay());
        intent.putExtra("hour", getHour());
        intent.putExtra("min", getMin());
        intent.putExtra("position", position);
        intent.putExtra("myId", myId);//ID 정보 intent
        startActivity(intent);
    }

    //닫기 버튼
    public void create_room_close(View view){
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeText = findViewById(R.id.time_Tview);
        if(hourOfDay < 12){
            timeText.setText("오전 " + hourOfDay + "시 " + minute + "분");
        }
        else if(hourOfDay == 12){
            timeText.setText("오후 " + hourOfDay + "시 " + minute + "분");
        }
        else{
            hourOfDay -= 12;
            timeText.setText("오후 " + hourOfDay + "시 " + minute + "분");
        }
        i_hour = hourOfDay;
        i_min = minute;
    }

    public int getYear(){ return i_year; }
    public int getMonth(){ return i_month; }
    public int getDay(){ return i_day; }
    public int getHour(){ return i_hour; }
    public int getMin(){ return i_min; }
}