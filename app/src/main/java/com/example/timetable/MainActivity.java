package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;

import com.example.timetable.datamodel.Item;
import com.example.timetable.ui.main.ReminderListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.View;

import com.example.timetable.ui.main.SectionsPagerAdapter;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    public static final int RQ_CODE_ADD_ACTIVITY = 301;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //view pager
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        //tabs
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //fab
        fab = findViewById(R.id.fab);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager.getCurrentItem() == 0) {
                    fab.animate().translationY(0f).scaleX(1).scaleY(1).setDuration(200L);
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() != 0) {
                    fab.animate().translationY(150f).scaleX(0).scaleY(0).setDuration(200L);
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (viewPager.getCurrentItem() == 0) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivityForResult(intent, RQ_CODE_ADD_ACTIVITY);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_CODE_ADD_ACTIVITY && resultCode == RESULT_OK) {
            Item newItem = new Item(-1);
            newItem.setSubject(data.getStringExtra(AddActivity.SUBJECT_TEXT));
            newItem.setTimeBegin(Time.valueOf(data.getStringExtra(AddActivity.START_TIME_TEXT) + ":00"));
            newItem.setDuration(data.getIntExtra(AddActivity.TIME_DURATION, 0));
            newItem.setComment(data.getStringExtra(AddActivity.COMMENT_TEXT));
            newItem.setDone(-1);
            ((ReminderListFragment) SectionsPagerAdapter.getPageInstance(0)).addItem(newItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("settings");
        menu.add("help");
        menu.add("about us");
        return super.onCreateOptionsMenu(menu);
    }
}