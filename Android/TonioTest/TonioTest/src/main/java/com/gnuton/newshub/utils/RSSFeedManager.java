package com.gnuton.newshub.utils;

import android.content.Context;

import com.gnuton.newshub.db.RSSEntryDataSource;
import com.gnuton.newshub.tasks.DownloadWebTask;
import com.gnuton.newshub.tasks.RSSParseTask;
import com.gnuton.newshub.types.RSSFeed;

import java.util.List;

/**
 * Created by gnuton on 6/5/13.
 */
public class RSSFeedManager extends Object{
    private RSSEntryDataSource mEntryDataSource = new RSSEntryDataSource(MyApp.getContext());

    // Singleton
    private static RSSFeedManager mInstance = null;
    public static RSSFeedManager getInstance() {
        if (mInstance == null) {
            mInstance = new RSSFeedManager();
        }
        return mInstance;
    }

    private RSSFeedManager() {

    }

    /**
     * This method
     * @param feed
     */
    public void requestEntryList(RSSFeed feed){
        // Get list from DB

        // Check if list is updated, if not download
    }

}
