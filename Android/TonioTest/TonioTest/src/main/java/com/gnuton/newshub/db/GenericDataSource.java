package com.gnuton.newshub.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gnuton on 5/25/13.
 */


public abstract class GenericDataSource {
    protected SQLiteDatabase database;
    protected DbHelper dbHelper;

    abstract String[] allColumns();

    public GenericDataSource(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    abstract public Serializable create(String[] record);

    abstract public void delete(Serializable record);

    abstract public List getAll();

    abstract Serializable cursorTo(Cursor cursor);
}