package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.timetable.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

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

        //fab
        final FloatingActionButton fab = findViewById(R.id.fab);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager.getCurrentItem() == 0) {
                    fab.animate().translationY(0f).scaleX(1).scaleY(1).setDuration(200L);
                    fab.setImageResource(R.drawable.round_add_white_48);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() != 0)
                    fab.animate().translationY(150f).scaleX(0).scaleY(0).setDuration(200L);
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
//                Bundle bundle=new Bundle();
                    startActivity(intent);
                }
            });
        }
    }
}