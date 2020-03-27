package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;

import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker;
import me.adawoud.bottomsheettimepicker.OnTimeRangeSelectedListener;

public class AddActivity extends AppCompatActivity implements OnTimeRangeSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
//        BottomSheetTimeRangePicker.Companion.newInstance(this, DateFormat.is24HourFormat(this))
//                .show(supportFragmentManager, "My Time :)");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onTimeRangeSelected(int i, int i1, int i2, int i3) {

    }
}
