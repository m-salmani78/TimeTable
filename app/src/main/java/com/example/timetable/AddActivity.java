package com.example.timetable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    public static final String SUBJECT_TEXT = "subject";
    public static final String START_TIME_TEXT = "beginning time";
    public static final String TIME_DURATION = "time duration";
    public static final String COMMENT_TEXT = "comment";
    EditText beginningTime, timeDuration, subject, comment;
    Button confirm_btn;
    Calendar now;
    TextView remaining_words_txt;
    int hourBegin, minuteBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        subject = findViewById(R.id.subject_ed_txt);
        comment = findViewById(R.id.comment_ed_txt);
        beginningTime = findViewById(R.id.beginning_time);
        timeDuration = findViewById(R.id.end_time);
        confirm_btn = findViewById(R.id.confirm_btn);
        remaining_words_txt = findViewById(R.id.remaining_words);


        //actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //time set
        now = Calendar.getInstance();
        hourBegin = now.get(Calendar.HOUR_OF_DAY);
        minuteBegin = now.get(Calendar.MINUTE);
        beginningTime.setOnClickListener(this);

        //confirm data
        confirm_btn.setOnClickListener(this);

        //update remaining words
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = comment.getText().toString().length() + "/" + 100;
                remaining_words_txt.setText(str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourBegin = timePicker.getHour();
        minuteBegin = timePicker.getMinute();
        String time = ((hourBegin < 10) ? "0" : "") + hourBegin + ":" + ((minuteBegin < 10) ? "0" : "") + minuteBegin;
        beginningTime.setText(time);
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
                        minuteBegin = now.get(Calendar.MINUTE);
                        beginningTime.setText("");
                    }
                });
                timePickerDialog.show();
                break;
            case R.id.confirm_btn:
                try {
                    if (beginningTime.getText().toString().trim().length() == 0 ||
                            subject.getText().toString().trim().length() <= 3 ||
                            timeDuration.getText().toString().trim().length() == 0) {
                        throw new RuntimeException();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(SUBJECT_TEXT, subject.getText().toString().trim());
                    intent.putExtra(START_TIME_TEXT, beginningTime.getText().toString());
                    intent.putExtra(TIME_DURATION, Integer.parseInt(timeDuration.getText().toString()));
                    intent.putExtra(COMMENT_TEXT, comment.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (RuntimeException e) {
                    warning();
                    break;
                }
        }
    }

    private void warning() {
        if (beginningTime.getText().toString().trim().length() == 0) {
            YoYo.with(Techniques.Shake).duration(500).playOn(beginningTime);
            beginningTime.setError(getResources().getString(R.string.warning_time));
        }
        if (timeDuration.getText().toString().trim().length() == 0) {
            YoYo.with(Techniques.Shake).duration(500).playOn(timeDuration);
            timeDuration.setError(getResources().getString(R.string.warning_duration));
        }
        if (subject.getText().toString().trim().length() <= 3) {
            YoYo.with(Techniques.Shake).duration(500).playOn(subject);
            subject.setError(getResources().getString(R.string.warning_subject));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
