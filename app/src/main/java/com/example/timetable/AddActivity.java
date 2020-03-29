package com.example.timetable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Calendar now;
    int hourBegin, minuteBegin, hourEnd, minuteEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        now = Calendar.getInstance();
        hourBegin = now.get(Calendar.HOUR_OF_DAY);
        hourEnd = hourBegin + 1;
        minuteBegin = minuteEnd = now.get(Calendar.MINUTE);

        editText = findViewById(R.id.range_time);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                        AddActivity.this, hourBegin, minuteBegin, true);
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        now = Calendar.getInstance();
                        hourBegin = now.get(Calendar.HOUR_OF_DAY);
                        hourEnd = hourBegin + 1;
                        minuteBegin = minuteEnd = now.get(Calendar.MINUTE);
                        editText.setText("");
                    }
                });
                timePickerDialog.show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourBegin = timePicker.getHour();
        minuteBegin = timePicker.getMinute();
        String time=((hourBegin < 10) ? "0" : "") + hourBegin + ":" + ((minuteBegin < 10) ? "0" : "") + minuteBegin;
        editText.setText(time);
    }
}
