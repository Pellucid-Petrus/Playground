package com.gnuton.newshub.utils;

import android.content.Context;

import com.gnuton.newshub.tasks.DownloadWebTask;
import com.gnuton.newshub.tasks.RSSParseTask;

import java.util.List;

/**
 * Created by gnuton on 6/5/13.
 */
public class RSSFeedManager extends Object implements DownloadWebTask.OnRequestCompletedListener, RSSParseTask.OnParsingCompletedListener{
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

    @Override
    public void onParsingCompleted(List list) {

    }

    @Override
    public void onRequestCompleted(String buffer) {

    }

}
