package com.example.timetable.datamodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppFeature {

    public static final int ID_POSTS_ACTIVITY = 0;
    public static final int ID_USER_PROFILE = 1;
    public static final int ID_FASHION = 2;
    public static final int ID_MUSIC = 3;
    public static final int ID_VIDEO = 4;
    public static final int ID_LOGIN = 5;

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

    public void goToAuthorWebPage(Context context){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(authorPageUrl));
        context.startActivity(intent);
    }
}
