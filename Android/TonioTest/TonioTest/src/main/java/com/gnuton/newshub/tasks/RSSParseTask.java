package com.gnuton.newshub.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.gnuton.newshub.db.DbHelper;
import com.gnuton.newshub.db.RSSEntryDataSource;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.types.RSSFeed;
import com.gnuton.newshub.utils.MyApp;
import com.gnuton.newshub.utils.XMLFeedParser;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


/**
 * Created by gnuton on 5/19/13.
 */
public class RSSParseTask extends AsyncTask<RSSFeed, Void, RSSFeed> {
    private static final String TAG = "RSS_PARSE_TASK" ;
    private static final RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());
    private static final int UPDATE_INTERVAL = 30;
    private static final int MILLISECONDS_IN_A_MINUTE = 60000;

    private static OnParsingCompletedListener listener;

    public RSSParseTask(Object o) {
        if (o instanceof OnParsingCompletedListener) {
            this.listener = (OnParsingCompletedListener) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement RSSParseTask.OnParsingCompletedListener");
        }
    }

    @Override
    protected RSSFeed doInBackground(RSSFeed... feeds) {
        try {
            RSSFeed feed = feeds[0];

            if (feed == null)
                return feed;

            Calendar rightNow = Calendar.getInstance();
            Calendar offset = Calendar.getInstance();
            offset.add(Calendar.MINUTE, UPDATE_INTERVAL);

            // Fetch data from the internet if this is the first time or if data is older than 30 mins
            if (feed.lastUpdate == null || feed.lastUpdate.compareTo(offset) > UPDATE_INTERVAL * MILLISECONDS_IN_A_MINUTE){

                Log.d(TAG, "Downloading entries from provider...");
                feed.xml = DownloadWebTask.downloadUrl(feed.url);
                if (feed.xml != null && feed.xml != "")
                    feed.lastUpdate = rightNow;

                new XMLFeedParser(eds).parseXML(feed);
            } else {
                Log.d(TAG, "RSS that we have looks to be updated.");
            }

            String selection = DbHelper.ENTRIES_FEEDID + " = " + String.valueOf(feed.id);
            String orderBy = DbHelper.ENTRIES_PUBLISHEDDATE +" DESC";
            feed.entries = eds.getAll(selection,null, null, null, orderBy);

            return feed;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(RSSFeed feed) {
        if (feed == null)
            return;

        List<RSSEntry> entries = feed.entries;
        if (entries.size() == 0) {
            Log.d(TAG, "ERROR: onPostExecute - no entries.");
            return;
        }

        new BoilerPipeTask().execute(entries.toArray(new RSSEntry[entries.size()]));

        listener.onParsingCompleted(feed);
    }

    public interface OnParsingCompletedListener {
        public void onParsingCompleted(final RSSFeed feed);
    }
}
