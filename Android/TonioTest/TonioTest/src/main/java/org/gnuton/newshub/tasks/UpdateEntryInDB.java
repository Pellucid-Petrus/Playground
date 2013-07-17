package org.gnuton.newshub.tasks;

import android.os.AsyncTask;

import org.gnuton.newshub.db.RSSEntryDataSource;
import org.gnuton.newshub.types.RSSEntry;
import org.gnuton.newshub.utils.MyApp;

/**
 * Created by gnuton on 6/10/13.
 */
public class UpdateEntryInDB extends AsyncTask<RSSEntry, Void, Void> {
    @Override
    protected Void doInBackground(RSSEntry... rssEntries) {
        RSSEntryDataSource eds = new RSSEntryDataSource(MyApp.getContext());
        for (RSSEntry e : rssEntries)
            eds.update(e);

        return null;
    }
}
