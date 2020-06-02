package com.example.timetable.ApiService;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.timetable.R;
import com.example.timetable.datamodel.AppFeature;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadImageTask extends AsyncTask<Void, Integer, Void> {
    private Context context;
    private OnImageDownload onImageDownload;
    private List<AppFeature> appFeatures;
    private ProgressBar progressBar;
    private ProgressBar linearProgress;
    private FloatingActionButton fab;

    public DownloadImageTask(Context context, OnImageDownload onImageDownload
            , @NonNull List<AppFeature> appFeatures, View root) {
        this.context = context;
        this.onImageDownload = onImageDownload;
        this.appFeatures = appFeatures;
        fab=root.findViewById(R.id.download_fab);
        progressBar=root.findViewById(R.id.progressBar2);
        linearProgress=root.findViewById(R.id.linearProgressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fab.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < appFeatures.size(); i++) {
            try {
                // determining the path of the file
                File extImageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                // create a file for image and name it
                File imageFile = new File(extImageDir, appFeatures.get(i).getId() + ".jpeg");
                if (imageFile.exists()) continue;
                //download image from server
                Bitmap bitmap = Picasso.get().load(appFeatures.get(i).getImageUrl()).get();
                // create file output stream
                FileOutputStream fos = new FileOutputStream(imageFile);
                //compress the image and determine it's format and quality
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            publishProgress(i * 100 / appFeatures.size());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Animation animation=AnimationUtils.loadAnimation(context,R.anim.progress_bar_animation);
        fab.startAnimation(animation);
        fab.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        onImageDownload.onReceived();
        linearProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        super.onProgressUpdate(percent);
        linearProgress.setProgress(percent[0]);
    }

    public interface OnImageDownload {
        void onReceived();
    }
}
