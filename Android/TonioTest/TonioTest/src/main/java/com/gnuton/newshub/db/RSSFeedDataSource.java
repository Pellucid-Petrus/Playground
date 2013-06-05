package com.gnuton.newshub.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.gnuton.newshub.types.RSSFeed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RSS Feed DAO class. It maintains the database connection and supports adding and fetching all feeds operations.
 * Created by gnuton on 5/25/13.
 */
public class RSSFeedDataSource extends GenericDataSource {
    private static final String TAG = RSSFeedDataSource.class.getName();

    public RSSFeedDataSource(Context context) {
        super(context);
        open();
    }

    @Override
    String[] allColumns() {
        return new String[] { DbHelper.ID, DbHelper.FEEDS_TITLE, DbHelper.FEEDS_URL};
    }


    @Override
    public Serializable create(String[] record) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.FEEDS_TITLE, record[0]);
        values.put(DbHelper.FEEDS_URL, record[1]);

        long insertId = database.insert(DbHelper.TABLE_FEEDS, null, values);
        Cursor cursor = database.query(DbHelper.TABLE_FEEDS,
                this.allColumns(), DbHelper.ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        RSSFeed feed = (RSSFeed) cursorTo(cursor);
        cursor.close();
        return feed;
    }

    public Serializable create(final RSSFeed f) {
        String title = f.title;
        String url = f. url;
        String[] record = {title, url};
        return this.create(record);
    }

    @Override
    public void delete(Serializable record) {
        RSSFeed feed = (RSSFeed) record;
        long id = feed.id;
        Log.d(TAG, "Feed deleted with id: " + id);
        database.delete(DbHelper.TABLE_FEEDS, DbHelper.ID + " = " + id, null);
    }

    @Override
    public List getAll() {
        List<RSSFeed> feeds = new ArrayList<RSSFeed>();
        Cursor cursor = database.query(DbHelper.TABLE_FEEDS, this.allColumns(), null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RSSFeed feed = (RSSFeed) cursorTo(cursor);
            feeds.add(feed);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return feeds;
    }

    @Override
    Serializable cursorTo(Cursor cursor) {
        final int id = cursor.getInt(0);
        final String title = cursor.getString(1);
        final String uri = cursor.getString(2);
        Log.d(TAG, "CURSOR " + id + " " + title + " " + uri);
        return new RSSFeed(id, title, uri);
    }
}
