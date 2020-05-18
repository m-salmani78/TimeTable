package com.example.timetable.ApiService;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.example.timetable.datamodel.AppFeature;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadImageTask extends AsyncTask<Void, Integer, Void> {
    private Context context;
    private OnImageDownload onImageDownload;
    private List<AppFeature> appFeatures;
    private ProgressDialog progressDialog;

    public DownloadImageTask(Context context, OnImageDownload onImageDownload, @NonNull List<AppFeature> appFeatures) {
        this.context = context;
        this.onImageDownload = onImageDownload;
        this.appFeatures = appFeatures;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("ذخیره سازی عکس ها");
        progressDialog.setMessage("لطفا منتظر بمانید");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
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
        progressDialog.hide();
        onImageDownload.onReceived(0);
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        super.onProgressUpdate(percent);
        progressDialog.setProgress(percent[0]);
    }

    public interface OnImageDownload {
        void onReceived(int position);
    }
}
