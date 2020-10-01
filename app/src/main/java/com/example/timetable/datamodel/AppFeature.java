package com.example.timetable.datamodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.timetable.AuthorPageActivity;
import com.example.timetable.R;

public class AppFeature {
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_IMAGE_ID = "image_id";
    public static final String KEY_AUTHOR_NAME = "author_name";
    private int id;
    private String title;
    private String imageUrl;
    private String authorPageUrl;

    public AppFeature() {
    }

    public AppFeature(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public AppFeature(int id, String title, String imageUrl, String authorPageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.authorPageUrl = authorPageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorPageUrl() {
        return authorPageUrl;
    }

    public void setAuthorPageUrl(String authorPageUrl) {
        this.authorPageUrl = authorPageUrl;
    }

    public void goToAuthorPage(Context context) {
        Intent intent = new Intent(context, AuthorPageActivity.class);
        String str = imageUrl.substring(0, imageUrl.lastIndexOf("/"));
        str = str.substring(0, imageUrl.lastIndexOf("/"));
        intent.putExtra(KEY_IMAGE_URL, str + "/600/400");
        intent.putExtra(KEY_IMAGE_ID, getId());
        intent.putExtra(KEY_AUTHOR_NAME, title);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.fragment_close_exit);
    }
}
