package com.example.sabrina.tasksfortigers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sabrina on 4/8/2015.
 */
public class TaskDBHelper extends SQLiteOpenHelper{

    static private final String DB_NAME = "task4tiger.db";
    static private final int DB_VERSION = 1;

    public TaskDBHelper( Context context ){
        super( context, DB_NAME, null, DB_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        TaskTable.onCreate( db );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TaskTable.onUpgrade(db, oldVersion, newVersion);
    }
}
