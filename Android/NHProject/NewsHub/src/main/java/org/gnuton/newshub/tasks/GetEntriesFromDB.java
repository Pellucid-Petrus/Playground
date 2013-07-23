package org.gnuton.newshub.tasks;

import android.os.AsyncTask;

import org.gnuton.newshub.db.DbHelper;
import org.gnuton.newshub.db.RSSEntryDataSource;
import org.gnuton.newshub.types.RSSEntry;
import org.gnuton.newshub.types.RSSFeed;
import org.gnuton.newshub.utils.MyApp;

import java.util.List;

/**
 * Created by gnuton on 6/27/13.
 */
public class GetEntriesFromDB extends AsyncTask<GetEntriesFromDB.GetEntriesRequest, Void, RSSFeed> {
    private static final RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());
    private final String TAG = getClass().getName();

    private final OnResultsGot mListener;

    public GetEntriesFromDB(Object o) {
        if (o instanceof OnResultsGot) {
            this.mListener = (OnResultsGot) o;
        } else {
            throw new ClassCastException(o.toString() + " must implement GetEntriesFromDB.OnResultsGot");
        }
    }

    @Override
    protected RSSFeed doInBackground(GetEntriesRequest... requests) {

        if (requests.length != 1){
            throw new RuntimeException("Requests length must be one.");
        }
        RSSFeed feed = requests[0].feed;
        feed.entries = this.getDataFromDB(requests[0]);

        return feed;
    }

    private List<RSSEntry> getDataFromDB(GetEntriesRequest request){
        RSSFeed feed = request.feed;
        return eds.getAll(request.selection, null, null, null, request.orderBy);
    }
    @Override
    protected void onPostExecute(RSSFeed feed) {
        mListener.onResultsGot(feed);
    }

    /** Inner interfaces **/
    public interface OnResultsGot {
        public void onResultsGot(final RSSFeed feed);
    }

    /** Inner classes **/
    public class GetEntriesRequest{
        private final  RSSFeed feed;
        private final String selection;
        private final String orderBy;

        GetEntriesRequest(RSSFeed feed, String selection, String orderBy) {
            this.feed = feed;
            this.selection = selection;
            this.orderBy = orderBy;
        }
    }

    /** builders **/
    public GetEntriesRequest createGetLatestEntriesRequest(RSSFeed feed){
        final String selection = DbHelper.ENTRIES_FEEDID + " = " + String.valueOf(feed.id);
        final String orderBy = DbHelper.ENTRIES_PUBLISHEDDATE +" DESC";
        return new GetEntriesRequest(feed, selection, orderBy);
    }
}

