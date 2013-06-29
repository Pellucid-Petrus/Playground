package com.gnuton.newshub.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.gnuton.newshub.R;
import com.gnuton.newshub.tasks.GetEntriesFromDB;
import com.gnuton.newshub.tasks.RSSParseTask;
import com.gnuton.newshub.types.RSSFeed;

import java.util.Calendar;

/**
 * Created by gnuton on 6/5/13.
 */
public class RSSFeedManager extends Object implements RSSParseTask.OnParsingCompletedListener, GetEntriesFromDB.OnResultsGot{
    private final String TAG = getClass().toString();
    private OnEntryListFetchedListener mListener;
    private static final int UPDATE_INTERVAL = 30;
    private static final int MILLISECONDS_IN_A_MINUTE = 60000;
    private GetEntriesFromDB mGetEntryTask;
    private RSSParseTask mParseTask;

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

        if (feed != null && feed.entries == null) {
            Notifications.showMsg(R.string.warning_no_entries_found);
            return;
        } else {
            Notifications.showMsg(R.string.msg_entry_list_updated);
        }
        mListener.setBusyIndicator(false);
        mListener.onEntryListFetched(feed);
    }

    /** Callback executed when GetEntriesFromDB gets something from DB **/
    @Override
    public void onResultsGot(RSSFeed feed) {
        if (feed != null){
            mListener.setBusyIndicator(false);
            mListener.onEntryListFetched(feed);
        }

        Calendar rightNow = Calendar.getInstance();
        Calendar offset = Calendar.getInstance();
        offset.add(Calendar.MINUTE, UPDATE_INTERVAL);

        // Fetch data from the internet if this is the first time or if data is older than 30 mins
        if (feed.lastUpdate == null || feed.lastUpdate.compareTo(offset) > UPDATE_INTERVAL * MILLISECONDS_IN_A_MINUTE){
            // Update data
            feed.lastUpdate = rightNow;
            mListener.setBusyIndicator(true);

            if (mParseTask != null)
                mParseTask.cancel(false);
            mParseTask = new RSSParseTask(this);
            mParseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, feed);
        } else {
            Log.d(TAG, "RSS that we have looks to be updated.");
        }
    }

    /**
     * This method is called when a client object asks for the list of entries associated on a feed
     * When the list is retrieved from DB and Internet, listener.onEntryListFetched callback is called.
     */
    public void requestEntryList(RSSFeed feed, OnEntryListFetchedListener listener){
        mListener = listener;

        // Read data from DB
        mListener.setBusyIndicator(true);
        if (mGetEntryTask != null)
            mGetEntryTask.cancel(false);
        mGetEntryTask = new GetEntriesFromDB(this);
        mGetEntryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mGetEntryTask.createGetLatestEntriesRequest(feed));
    }

    public interface OnEntryListFetchedListener{
        public void onEntryListFetched(RSSFeed feed);
        public void setBusyIndicator(Boolean on);
    }
}
