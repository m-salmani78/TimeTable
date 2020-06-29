package com.example.timetable.appFeature;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.datamodel.AppFeature;

import java.io.File;
import java.util.List;

import static com.example.timetable.ui.main.GalleryFragment.KEY_PAGE_NUM;
import static com.example.timetable.ui.main.GalleryFragment.SHARED_PREF_NAME;

public class AppFeaturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AppFeaturesAdapter";
    private static final int VIEW_TYPE_HEADER = 292;
    private static final int VIEW_TYPE_DEFAULT_ITEM = 266;

    private Context context;
    private List<AppFeature> appFeatures;

    public AppFeaturesAdapter(Context context, List<AppFeature> appFeatures) {
        this.context = context;
        this.appFeatures = appFeatures;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_gallery_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_app_feature, parent, false);
            return new AppFeatureViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (position < getItemCount() - 1) {
            AppFeatureViewHolder appFeatureHolder = (AppFeatureViewHolder) holder;
            File extImageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(extImageDir.getAbsolutePath(), appFeatures.get(position).getId() + ".jpeg");
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                appFeatureHolder.imageView.setImageBitmap(bitmap);
            } else {
                appFeatureHolder.imageView.setImageResource(R.drawable.unload_image);
            }
            appFeatureHolder.chapterTitle.setText(appFeatures.get(position).getTitle());
            appFeatureHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appFeatures.get(position).goToAuthorPage(context);
                }
            });
            appFeatureHolder.webPageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appFeatures.get(position).getAuthorPageUrl()));
                    context.startActivity(intent);
                }
            });
        } else {
            HeaderViewHolder headerView = (HeaderViewHolder) holder;
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            int page = sharedPreferences.getInt(KEY_PAGE_NUM, 0);
            String pgStr=page+"";
            headerView.pageNumEdTxt.setText(pgStr);
            String countStr=(getItemCount() - 1) + "";
            headerView.limitEdTxt.setText(countStr);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_DEFAULT_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return appFeatures.size() + 1;
    }


    private class AppFeatureViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView chapterTitle;
        private ImageButton webPageBtn, shareBtn;

        AppFeatureViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feature_image_view);
            chapterTitle = itemView.findViewById(R.id.feature_title);
            webPageBtn = itemView.findViewById(R.id.web_page_btn);
            shareBtn = itemView.findViewById(R.id.share_btn);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView pageNumEdTxt;
        private TextView limitEdTxt;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            pageNumEdTxt = itemView.findViewById(R.id.page_txt);
            limitEdTxt = itemView.findViewById(R.id.limitation_txt);
        }
    }
}
