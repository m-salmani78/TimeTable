package com.example.timetable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    public static final String SUBJECT_TEXT = "subject";
    public static final String START_TIME_TEXT = "beginning time";
    public static final String END_TIME_TEXT = "end time";
    public static final String COMMENT_TEXT = "comment";
    EditText beginningTime, endTime, subject, comment;
    Button confirm_btn;
    Calendar now;
    int hourBegin, minuteBegin, hourEnd, minuteEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        subject = findViewById(R.id.subject_ed_txt);
        comment = findViewById(R.id.comment_ed_txt);
        //time set
        now = Calendar.getInstance();
        hourBegin = now.get(Calendar.HOUR_OF_DAY);
        hourEnd = hourBegin + 1;
        minuteBegin = minuteEnd = now.get(Calendar.MINUTE);
        beginningTime = findViewById(R.id.beginning_time);
        endTime = findViewById(R.id.end_time);
        beginningTime.setOnClickListener(this);
        //confirm data
        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourBegin = timePicker.getHour();
        minuteBegin = timePicker.getMinute();
        String time = ((hourBegin < 10) ? "0" : "") + hourBegin + ":" + ((minuteBegin < 10) ? "0" : "") + minuteBegin;
        beginningTime.setText(time);
        endTime.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beginning_time:
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                        AddActivity.this, hourBegin, minuteBegin, true);
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        now = Calendar.getInstance();
                        hourBegin = now.get(Calendar.HOUR_OF_DAY);
                        hourEnd = hourBegin + 1;
                        minuteBegin = minuteEnd = now.get(Calendar.MINUTE);
                        beginningTime.setText("");
                    }
                });
                timePickerDialog.show();
            case R.id.confirm_btn:
                try {
                    if (beginningTime.getText().toString().trim().length() == 0 ||
                            subject.getText().toString().trim().length() <= 3 ||
                            endTime.getText().toString().trim().length() == 0)
                        throw new RuntimeException();
                    Intent intent = new Intent();
                    intent.putExtra(SUBJECT_TEXT, subject.getText().toString().trim());
                    intent.putExtra(START_TIME_TEXT, beginningTime.getText().toString().trim());
                    intent.putExtra(END_TIME_TEXT, endTime.getText().toString().trim());
                    intent.putExtra(COMMENT_TEXT, comment.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (RuntimeException e){
                warning();
            }
        }
    }

    private void warning() {
        //TODO
    }
}
