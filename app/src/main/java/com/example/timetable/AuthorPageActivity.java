package com.example.timetable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.example.timetable.datamodel.AppFeature;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class AuthorPageActivity extends AppCompatActivity {
    private static boolean favorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);
        Slidr.attach(this);
        Intent intent = getIntent();

        //toolbar
        Toolbar toolbar = findViewById(R.id.author_page_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //status bar change color
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(intent.getStringExtra(AppFeature.KEY_AUTHOR_NAME));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.parseColor("#10000000"));
//        }

        //header image
        ImageView imageView = findViewById(R.id.author_image);

        File extImageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (extImageDir == null) return;
        File file = new File(extImageDir.getAbsolutePath(), intent.getIntExtra(AppFeature.KEY_IMAGE_ID, 0) + ".jpeg");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.unload_image);
        }

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "you " + (favorite ? "unlike" : "like") + " picture", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                if (!favorite) {
                    fab.setImageResource(R.drawable.ic_heart_fill_24dp);
                    favorite = true;
                } else {
                    fab.setImageResource(R.drawable.ic_heart_outline_24dp);
                    favorite = false;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.fragment_open_enter, R.anim.slide_out_right);
        return true;
    }
}
