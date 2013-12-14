package org.gnuton.newshub.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.gnuton.newshub.R;
import org.gnuton.newshub.tasks.GetEntriesFromDB;
import org.gnuton.newshub.tasks.RSSParseTask;
import org.gnuton.newshub.types.RSSFeed;

import java.util.Calendar;


public class RSSFeedManager implements RSSParseTask.OnParsingCompletedListener, GetEntriesFromDB.OnResultsGot{
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResultsGot(RSSFeed feed) {
        if (feed != null && feed.entries.size() != 0){
            mListener.setBusyIndicator(false);
            mListener.onEntryListFetched(feed);
        }

        Calendar rightNow = Calendar.getInstance();
        Calendar offset = Calendar.getInstance();
        offset.add(Calendar.MINUTE, UPDATE_INTERVAL);

        // Fetch data from the internet if this is the first time or if data is older than 30 mins
        assert feed != null;
        if (feed.lastUpdate == null || feed.entries.size() == 0 ||
                feed.lastUpdate.compareTo(offset) > UPDATE_INTERVAL * MILLISECONDS_IN_A_MINUTE){
            // Update data
            feed.lastUpdate = rightNow;
            mListener.setBusyIndicator(true);

            if (mParseTask != null)
                mParseTask.cancel(false);
            mParseTask = new RSSParseTask(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mParseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, feed);
            else
                mParseTask.execute(feed);
        } else {
            Log.d(TAG, "RSS that we have looks to be updated.");
        }
    }

    /**
     * This method is called when a client object asks for entries
     * When the list is retrieved from DB or Internet, listener.onEntryListFetched callback is called.
     */
    public void requestEntryList(RSSFeed feed, OnEntryListFetchedListener listener){
        mListener = listener;

        // UI shows busy indicator
        mListener.setBusyIndicator(true);

        // Terminate any previous tasks
        if (mGetEntryTask != null)
            mGetEntryTask.cancel(false);


        // Feed in DB. let's fetch some data!
        mGetEntryTask = new GetEntriesFromDB(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mGetEntryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mGetEntryTask.createGetLatestEntriesRequest(feed));
        else
            mGetEntryTask.execute(mGetEntryTask.createGetLatestEntriesRequest(feed));

    }

    public interface OnEntryListFetchedListener{
        public void onEntryListFetched(RSSFeed feed);
        public void setBusyIndicator(Boolean on);
    }
}
