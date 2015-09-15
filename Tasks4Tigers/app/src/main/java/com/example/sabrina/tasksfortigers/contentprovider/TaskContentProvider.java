package com.example.sabrina.tasksfortigers.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.sabrina.tasksfortigers.db.TaskDBHelper;
import com.example.sabrina.tasksfortigers.db.TaskTable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Sabrina on 4/8/2015.
 */
public class TaskContentProvider extends ContentProvider {

    private TaskDBHelper db;

    static private final String AUTHORITY
            = "com.example.sabrina.tasksfortigers.provider";

    static private final String BASE_PATH
            = "tasks";

    static public final String CONTENT_TYPE
            = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tasks";

    static public final String CONTENT_ITEM_TYPE
            = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";

    static public final Uri CONTENT_URI =
             Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Set up the URLMatcher
    static private final UriMatcher sURIMatcher // because this is a static var and not a instance var use lowercase s
            = new UriMatcher( UriMatcher.NO_MATCH);
    static private final int TASKS = 1;
    static private final int TASK_ID = 2;
    // this is used to execute this block of code
    // exist only once the class is loaded and that's it
    // meaning "Hey I have a scope that I need to execute once the class is loaded"
    static{
        sURIMatcher.addURI( AUTHORITY, BASE_PATH, TASKS );
        sURIMatcher.addURI( AUTHORITY, BASE_PATH + "/#", TASK_ID );
    }

    public boolean onCreate() {
        db = new TaskDBHelper( getContext() );
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // projection is going to say which of those columns to return

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns( projection ); // verify that all of the requested columns exist

        queryBuilder.setTables( TaskTable.TASK_TABLE );

        int uriType = sURIMatcher.match( uri );
        switch ( uriType ){
            case TASKS:
                break;
            case TASK_ID:
                queryBuilder.appendWhere(TaskTable.COLUMN_ID
                +  " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase wdb = db.getWritableDatabase();
        Cursor cursor = queryBuilder.query( wdb, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri( getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, // Where we want to put something
                      ContentValues values) { // What we are going to put
        // what kind of uri was I given
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase wdb = db.getWritableDatabase();
        long id = 0;

        switch ( uriType ) {
            case TASKS:
                id = wdb.insert(TaskTable.TASK_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext(). getContentResolver().notifyChange(uri, null);
         return Uri.parse(AUTHORITY + "/" + BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase wdb = db.getWritableDatabase();
        int rowsDeleted = 0;

        switch ( uriType ) {
            case TASKS:
                rowsDeleted = wdb.delete( TaskTable.TASK_TABLE, selection, selectionArgs );
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty( selection )){
                    rowsDeleted = wdb.delete(TaskTable.TASK_TABLE, TaskTable.COLUMN_ID + "=" + id, null);
                }else {
                    rowsDeleted = wdb.delete(TaskTable.TASK_TABLE, TaskTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase wdb = db.getWritableDatabase();
        int rowsUpdated = 0;

        switch ( uriType ) {
            case TASKS:
                    rowsUpdated = wdb.update( TaskTable.TASK_TABLE, values, selection, selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = wdb.update( TaskTable.TASK_TABLE, values, TaskTable.COLUMN_ID + "=" + id, null);
                }else{
                    rowsUpdated = wdb.update( TaskTable.TASK_TABLE, values, TaskTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowsUpdated;
    }

    private void checkColumns(String[] projection){
        String[] available = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_PRIORITY, TaskTable.COLUMN_SUMMARY};

        if ( projection != null ){
            // an element can only appear here ones
            HashSet<String> requestedCColumns = new HashSet<String>(Arrays.asList( projection ));
            HashSet<String> availableCColumns = new HashSet<String>(Arrays.asList( projection ));

            if ( !availableCColumns.contains( requestedCColumns)){
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
