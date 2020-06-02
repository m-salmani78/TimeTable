package com.example.timetable.datamodel;

import android.content.Context;
import android.content.Intent;

import com.example.timetable.AuthorPageActivity;

public class AppFeature {

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
//        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(authorPageUrl));
        Intent intent = new Intent(context, AuthorPageActivity.class);
        String str = imageUrl.substring(0, imageUrl.lastIndexOf("/"));
        str = str.substring(0, imageUrl.lastIndexOf("/"));
        intent.putExtra("image_url", str+"/600/400");
        intent.putExtra("author_name",title);
        context.startActivity(intent);
    }
}
