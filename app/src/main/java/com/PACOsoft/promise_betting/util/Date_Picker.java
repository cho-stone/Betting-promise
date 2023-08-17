package com.PACOsoft.promise_betting.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.DatePicker;

import com.PACOsoft.promise_betting.view.Create_Room;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Date_Picker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year, month, day;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), this, year, month, day);
        Calendar minDate = Calendar.getInstance(); //캘린더에서 선택할 수 있는 최소날짜

        minDate.set(year, month, day);
        dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Create_Room activity = (Create_Room) getActivity();
        activity.processDatePickerResult(year, month, day);
    }
}