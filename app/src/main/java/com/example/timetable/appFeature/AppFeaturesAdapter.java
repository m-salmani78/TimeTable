package com.example.timetable.appFeature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.datamodel.AppFeature;

import java.io.File;
import java.util.List;

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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appFeatures.get(position).goToAuthorWebPage(context);
                }
            });
        } else {
            HeaderViewHolder headerView = (HeaderViewHolder) holder;
            headerView.pageNumEdTxt.setText("2");
            headerView.limitEdTxt.setText("10");
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

        AppFeatureViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feature_image_view);
            chapterTitle = itemView.findViewById(R.id.feature_title);
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
