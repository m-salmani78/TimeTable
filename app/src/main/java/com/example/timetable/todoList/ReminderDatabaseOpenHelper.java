package com.example.timetable.todoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.timetable.datamodel.Item;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String TAG = "DatabaseOpenHelper";
    public static final String DB_NAME = "db_reminder_list";
    private static final String TASKS_TABLE_NAME = "tbl_tasks";
    private static final String COL_SUBJECT = "col_subj";
    private static final String COL_ID = "col_id";
    private static final String COL_COMMENT = "col_comment";
    private static final String COL_TIME = "col_time";
    private static final String COL_DURATION = "col_duration";
    private static final String COL_IS_DONE = "col_done";
    private static final String SQL_COMMAND_CREATE = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SUBJECT + " TEXT, " + COL_IS_DONE + " INTEGER, " +
            COL_COMMENT + " TEXT, " + COL_TIME + " TEXT, " + COL_DURATION + " INTEGER)";
    private Context context;

    public ReminderDatabaseOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
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

    public long addItem(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SUBJECT, item.getSubject());
        contentValues.put(COL_COMMENT, item.getComment());
        contentValues.put(COL_TIME, item.getTimeBegin().toString());
        contentValues.put(COL_DURATION, item.getDuration());
        contentValues.put(COL_IS_DONE, item.isDone());
        SQLiteDatabase database = this.getWritableDatabase();
        long isInserted = database.insert(TASKS_TABLE_NAME, null, contentValues);
        Log.i(TAG, "addItem: " + isInserted);
        database.close();
        return isInserted;
    }

    public void addItems(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            addItem(items.get(i));
        }
    }

    public List<Item> getItems(List<Item> items) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME, null);
        items.clear();
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getInt(0));
                item.setSubject(cursor.getString(1));
                item.setDone(cursor.getInt(2));
                item.setComment(cursor.getString(3));
                item.setTimeBegin(Time.valueOf(cursor.getString(4)));
                item.setDuration(cursor.getInt(5));
                items.add(item);
            } while (cursor.moveToNext());
        }
        database.close();
        return items;
    }

    public List<Item> getItems() {
        return getItems(new ArrayList<Item>());
    }

    public void setIsDone(int itemID, int isDone) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IS_DONE, isDone);
        database.update(TASKS_TABLE_NAME, contentValues, COL_ID + " = " + itemID, null);
        database.close();
    }

    public boolean checkItemExists(int itemId) {
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
