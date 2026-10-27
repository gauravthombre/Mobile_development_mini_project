package com.example.studytaskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasks.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "tasks";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "subject TEXT, "
                + "due_date TEXT, "
                + "priority TEXT, "
                + "done INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addTask(String title, String subject, String dueDate, String priority) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("subject", subject);
        values.put("due_date", dueDate);
        values.put("priority", priority);
        values.put("done", 0);
        getWritableDatabase().insert(TABLE, null, values);
    }

    // Pending tasks first, newest first
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, title, subject, due_date, priority, done FROM " + TABLE
                        + " ORDER BY done ASC, id DESC", null);
        while (c.moveToNext()) {
            list.add(new Task(c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4), c.getInt(5) == 1));
        }
        c.close();
        return list;
    }

    public void setDone(int id, boolean done) {
        ContentValues values = new ContentValues();
        values.put("done", done ? 1 : 0);
        getWritableDatabase().update(TABLE, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        getWritableDatabase().delete(TABLE, "id = ?", new String[]{String.valueOf(id)});
    }

    public int getPendingCount() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + TABLE + " WHERE done = 0", null);
        int count = c.moveToFirst() ? c.getInt(0) : 0;
        c.close();
        return count;
    }
}
