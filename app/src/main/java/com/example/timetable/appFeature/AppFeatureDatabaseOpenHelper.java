package com.example.timetable.appFeature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.timetable.datamodel.AppFeature;

import java.util.ArrayList;
import java.util.List;

public class AppFeatureDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String TAG = "PicDatabaseOpenHelper";
    private static final String DB_NAME = "db_pictures";
    private static final String TASKS_TABLE_NAME = "tbl_pic";
    private static final String COL_ID = "col_id";
    private static final String COL_TITLE = "col_name";
    private static final String COL_DOWNLOAD_URL = "col_download_url";
    private static final String COL_AUTHOR_URL = "col_url";
    private static final String COL_PAGE = "col_page";

    private static final String SQL_COMMAND_CREATE = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME
            + "(" + COL_ID + " INTEGER PRIMARY KEY, " + COL_TITLE + " TEXT, "
            + COL_DOWNLOAD_URL + " TEXT, " + COL_AUTHOR_URL + " TEXT, " + COL_PAGE + " INTEGER)";

    public AppFeatureDatabaseOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_COMMAND_CREATE);
            Log.i(TAG, "onCreate: " + TASKS_TABLE_NAME);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addItem(AppFeature appFeature, int page) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, appFeature.getId());
        contentValues.put(COL_TITLE, appFeature.getTitle());
        contentValues.put(COL_DOWNLOAD_URL, appFeature.getImageUrl());
        contentValues.put(COL_AUTHOR_URL, appFeature.getAuthorPageUrl());
        contentValues.put(COL_PAGE, page);
        SQLiteDatabase database = this.getWritableDatabase();
        long rowInserted = database.insert(TASKS_TABLE_NAME, null, contentValues);
        Log.i(TAG, "addItem: " + rowInserted);
        database.close();
    }

    public void addItems(List<AppFeature> appFeatures, int page) {
        for (int i = 0; i < appFeatures.size(); i++) {
            addItem(appFeatures.get(i), page);
        }
    }

    public List<AppFeature> getItems(List<AppFeature> appFeatures, int page) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        if (page > 0) {
            cursor = database.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME
                    + " WHERE " + COL_PAGE + " = " + page, null);
        } else {
            cursor = database.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME, null);
        }
        appFeatures.clear();
        if (cursor.moveToFirst()) {
            do {
                AppFeature appFeature = new AppFeature();
                appFeature.setId(cursor.getInt(0));
                appFeature.setTitle(cursor.getString(1));
                appFeature.setImageUrl(cursor.getString(2));
                appFeature.setAuthorPageUrl(cursor.getString(3));
                appFeatures.add(appFeature);
            } while (cursor.moveToNext());
        }
        database.close();
        return appFeatures;
    }

    public List<AppFeature> getItems(int page) {
        return getItems(new ArrayList<AppFeature>(), page);
    }

    public boolean checkIsIdExists(int itemId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME +
                " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(itemId)});
        database.close();
        return cursor.moveToFirst();
    }

    public void deleteItem(int itemId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TASKS_TABLE_NAME, COL_ID + " = " + itemId, null);
        database.close();
    }

    public void deleteAllItems() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TASKS_TABLE_NAME, null, null);
        database.close();
    }
}
