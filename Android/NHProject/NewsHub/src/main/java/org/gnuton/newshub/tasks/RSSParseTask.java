package org.gnuton.newshub.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.gnuton.newshub.R;
import org.gnuton.newshub.db.RSSEntryDataSource;
import org.gnuton.newshub.types.RSSEntry;
import org.gnuton.newshub.types.RSSFeed;
import org.gnuton.newshub.utils.MyApp;
import org.gnuton.newshub.utils.Notifications;
import org.gnuton.newshub.utils.XMLFeedParser;

import java.io.IOException;
import java.util.List;

public class RSSParseTask extends AsyncTask<RSSFeed, Void, RSSFeed> {
    private static final String TAG = RSSParseTask.class.getName();
    private static final RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());

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

            Log.d(TAG, "Downloading entries from provider...");

            //TODO This is code is bad designed!!! downloadwebtask should be return an encoded str
            feed.xml = DownloadWebTask.downloadUrl(feed.url);


            return new XMLFeedParser(eds).parseXML(feed);
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
            Notifications.showErrorMsg(R.string.rssParseTaskNoEntries);

        }

        //new BoilerPipeTask().execute(entries.toArray(new RSSEntry[entries.size()]));

        listener.onParsingCompleted(feed);
    }

    public interface OnParsingCompletedListener {
        public void onParsingCompleted(final RSSFeed feed);
    }
}
