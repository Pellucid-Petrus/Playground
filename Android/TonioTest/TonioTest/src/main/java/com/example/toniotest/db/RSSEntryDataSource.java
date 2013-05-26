package com.example.toniotest.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import com.example.toniotest.types.RSSEntry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by gnuton on 5/26/13.
 */
public class RSSEntryDataSource extends GenericDataSource {
    private static final String TAG = RSSEntryDataSource.class.getName();

    public RSSEntryDataSource(Context context) {
        super(context);
        open();
    }

    @Override
    String[] allColumns() {
        return new String[] {DbHelper.ID,
                DbHelper.ENTRIES_TITLE,
                DbHelper.ENTRIES_SUMMARY,
                DbHelper.ENTRIES_URL,
                DbHelper.ENTRIES_CONTENT,
                DbHelper.ENTRIES_PUBLISHEDDATE
        };
    }

    @Override
    public Serializable create(String[] record) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.ENTRIES_TITLE, record[0]);
        values.put(DbHelper.ENTRIES_SUMMARY, record[1]);
        values.put(DbHelper.ENTRIES_URL, record[2]);
        values.put(DbHelper.ENTRIES_CONTENT, record[3]);
        values.put(DbHelper.ENTRIES_PUBLISHEDDATE, record[4]);

        long insertId = database.insert(DbHelper.TABLE_ENTRIES, null, values);
        Cursor cursor = database.query(DbHelper.TABLE_ENTRIES,
                this.allColumns(), DbHelper.ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        RSSEntry entry = (RSSEntry) cursorTo(cursor);
        cursor.close();
        return entry;
    }

    @Override
    public void delete(Serializable record) {
        RSSEntry entry = (RSSEntry) record;
        long id = entry.id;
        Log.d(TAG, "Entry deleted with id: " + id);
        database.delete(DbHelper.TABLE_ENTRIES, DbHelper.ID + " = " + id, null);
    }

    @Override
    public List getAll() {
        List<RSSEntry> entries = new ArrayList<RSSEntry>();
        Cursor cursor = database.query(DbHelper.TABLE_ENTRIES, this.allColumns(), null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RSSEntry entry = (RSSEntry) cursorTo(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    Serializable cursorTo(Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(DbHelper.ID));
        final String title = cursor.getString(cursor.getColumnIndex(DbHelper.ENTRIES_TITLE));
        final String summary = cursor.getString(cursor.getColumnIndex(DbHelper.ENTRIES_SUMMARY));
        final String url = cursor.getString(cursor.getColumnIndex(DbHelper.ENTRIES_URL));
        final String content = cursor.getString(cursor.getColumnIndex(DbHelper.ENTRIES_CONTENT));
        final String publishedDataString = cursor.getString(cursor.getColumnIndex(DbHelper.ENTRIES_PUBLISHEDDATE));

        XMLGregorianCalendar publishedData = null;
        try {
            publishedData = DatatypeFactory.newInstance().newXMLGregorianCalendar(publishedDataString);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"PUBLISHED DATE" + publishedData.toString());

        RSSEntry entry = new RSSEntry(id, title, summary, url, publishedData);
        entry.content = content;
        return entry;
    }
}
