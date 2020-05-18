package com.example.timetable.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.ApiService.DownloadImageTask;
import com.example.timetable.ApiService.RandomPictureApiService;
import com.example.timetable.R;
import com.example.timetable.appFeature.AppFeatureDatabaseOpenHelper;
import com.example.timetable.appFeature.AppFeaturesAdapter;
import com.example.timetable.datamodel.AppFeature;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements RandomPictureApiService.OnPicturesReceived {
    private static final int REQUEST_CODE = 698;
    private static final String TAG = "GalleryFragment";
    private AppFeaturesAdapter appFeaturesAdapter;
    private List<AppFeature> appFeatures = new ArrayList<>();
    private RandomPictureApiService pictureApiService;
    private AppFeatureDatabaseOpenHelper databaseOpenHelper;
    private EditText limitationEdTxt, pageNumEdTxt;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "app_feature_shared_pref";
    private static final String KEY_PAGE_NUM = "key_page_num";
    private static final String KEY_LIMIT = "key_limitation";


    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        FloatingActionButton fab = root.findViewById(R.id.download_fab);
        pageNumEdTxt = root.findViewById(R.id.page_ed_txt);
        limitationEdTxt = root.findViewById(R.id.limitation_ed_txt);

        pictureApiService = new RandomPictureApiService(getContext());

        //load items
        databaseOpenHelper = new AppFeatureDatabaseOpenHelper(getContext());
        databaseOpenHelper.getItems(appFeatures, sharedPreferences.getInt(KEY_PAGE_NUM, 1));

        //recycler view
        RecyclerView recyclerView = root.findViewById(R.id.image_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == appFeaturesAdapter.getItemCount() - 1) ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        appFeaturesAdapter = new AppFeaturesAdapter(getContext(), appFeatures);
        recyclerView.setAdapter(appFeaturesAdapter);

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer();
            }
        });
        return root;
    }

    public void getDataFromServer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check if the permission is granted or not
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //do the tasks
                pictureApiService.getPicturesFromServer(GalleryFragment.this, getPageNum(), getLimitation());
            } else {
                //ask the permission from user
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            pictureApiService.getPicturesFromServer(GalleryFragment.this, getPageNum(), getLimitation());
        }
    }

    public int getPageNum() {
        try {
            return Integer.parseInt(pageNumEdTxt.getText().toString().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int getLimitation() {
        try {
            return Integer.parseInt(limitationEdTxt.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    @Override
    public void onReceived(final List<AppFeature> list) {
        appFeatures.clear();
        DownloadImageTask downloadImageTask = new DownloadImageTask(getContext(), new DownloadImageTask.OnImageDownload() {
            @Override
            public void onReceived(int position) {
                appFeatures.addAll(list);
                appFeaturesAdapter.notifyDataSetChanged();
            }
        }, list);

        downloadImageTask.execute();
        databaseOpenHelper.addItems(list, getPageNum());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PAGE_NUM, getPageNum());
        editor.putInt(KEY_LIMIT, getLimitation());
        editor.apply();
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "an error happened :/", Toast.LENGTH_SHORT).show();
    }

    /**
     * do the tasks if the permission is granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                pictureApiService.getPicturesFromServer(GalleryFragment.this, getPageNum(), getLimitation());
            } else {
                Toast.makeText(getContext(), "permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
