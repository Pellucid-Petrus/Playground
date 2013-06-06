package com.gnuton.newshub.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gnuton.newshub.EntryListAdapter;
import com.gnuton.newshub.R;
import com.gnuton.newshub.db.RSSEntryDataSource;
import com.gnuton.newshub.tasks.DownloadWebTask;
import com.gnuton.newshub.tasks.RSSParseTask;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.types.RSSFeed;

import java.util.List;

/**
 * Created by gnuton on 6/5/13.
 */
public class RSSFeedManager extends Object implements RSSParseTask.OnParsingCompletedListener{
    private final String TAG = getClass().toString();
    private OnEntryListFetchedListener mListener;

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


    // Callback executed when parsing is completed
    @Override
    public void onParsingCompleted(final RSSFeed feed) {
        Log.d(TAG, "Parsing completed");

        Context context = MyApp.getContext();
        if (feed.entries == null) {
            Notifications.showWarning(R.string.warning_no_entries_found);
            return;
        }
        mListener.onEntryListFetched(feed);
    }
    /**
     * This method is called when a client object asks for the list of entries associated on a feed
     * When the list is retrieved from DB and Internet, listener.onEntryListFetched callback is called.
     */
    public void requestEntryList(RSSFeed feed, OnEntryListFetchedListener listener){
        mListener = listener;
        new RSSParseTask(this).execute(feed);
    }

    public interface OnEntryListFetchedListener{
        public void onEntryListFetched(RSSFeed feed);
    }
}
