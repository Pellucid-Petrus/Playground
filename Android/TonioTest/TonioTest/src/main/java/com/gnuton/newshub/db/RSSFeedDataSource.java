package com.gnuton.newshub.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.gnuton.newshub.R;
import com.gnuton.newshub.types.RSSFeed;
import com.gnuton.newshub.utils.Notifications;

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
        String title = record[0];
        String url = record[1];

        // Do not double records
        String selection = DbHelper.FEEDS_URL + " = " + DatabaseUtils.sqlEscapeString(url);

        List<RSSFeed> feeds = this.getAll(selection, null, null, null, null);
        if (feeds.size() != 0){
            Log.d(TAG, title + "is already in the DB");
            Notifications.showWarning(R.string.warning_feed_already_in_db);
            if (feeds.size() > 1)
                Log.d(TAG, "WARNING: More than one feed has same url");
            return feeds.get(0);
        }
        Log.d(TAG, "No similar item found in the DB. Adding new feed ...");

        // Create new record in the DB
        ContentValues values = new ContentValues();
        values.put(DbHelper.FEEDS_TITLE, title);
        values.put(DbHelper.FEEDS_URL, url);

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
    public List getAll(){
        return getAll(null, null, null, null, null);
    }

    public List getAll(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<RSSFeed> feeds = new ArrayList<RSSFeed>();
        Cursor cursor = database.query(DbHelper.TABLE_FEEDS, this.allColumns(), selection, selectionArgs, groupBy, having, orderBy);
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
