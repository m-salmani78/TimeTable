package com.example.timetable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class AuthorPageActivity extends AppCompatActivity {
    private static boolean favorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();

        //toolbar
        setContentView(R.layout.activity_author_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //status bar change color
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(intent.getStringExtra("author_name"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#10000000"));
        }

        //
        ImageView imageView = findViewById(R.id.author_image);
        imageView.setImageResource(R.drawable.nature_wallpaper);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "you "+(favorite?"unlike":"like")+" picture", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                if(!favorite){
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    favorite=true;
                }else {
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    favorite=false;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
