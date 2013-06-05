package com.gnuton.newshub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gnuton on 5/25/13.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper mInstance = null;

    private static final String DATABASE_NAME = "rssdb";
    private static final int DATABASE_VERSION = 9;

    public static final String TABLE_FEEDS = "feeds";
    public static final String TABLE_ENTRIES = "entries";

    public static final String ID = "_id";
    public static final String FEEDS_TITLE = "title";
    public static final String FEEDS_URL = "url";

    public static final String ENTRIES_FEEDID = "feedid";
    public static final String ENTRIES_TITLE = "title";
    public static final String ENTRIES_URL = "url";
    public static final String ENTRIES_SUMMARY = "summary";
    public static final String ENTRIES_CONTENT = "content";
    public static final String ENTRIES_PUBLISHEDDATE = "date";

    private static final String TAG = DbHelper.class.getName();

    private static final String CREATE_FEEDS_TABLE = "create table "
            + TABLE_FEEDS
            + "("
            + ID + " integer primary key autoincrement, "
            + FEEDS_TITLE + " text not null, "
            + FEEDS_URL + " text not null"
            + ");";
    private static final String CREATE_ENTRIES_TABLE = "create table "
            + TABLE_ENTRIES
            + "("
            + ID + " integer primary key autoincrement, "
            + ENTRIES_FEEDID + " integer not null, "
            + ENTRIES_TITLE + " text not null, "
            + ENTRIES_URL + " text not null, "
            + ENTRIES_SUMMARY + " text not null, "
            + ENTRIES_CONTENT + " text, "
            + ENTRIES_PUBLISHEDDATE + " text not null "
            + ");";


    public static DbHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Performing "+ CREATE_FEEDS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FEEDS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);

    onCreate(sqLiteDatabase);
    }
}