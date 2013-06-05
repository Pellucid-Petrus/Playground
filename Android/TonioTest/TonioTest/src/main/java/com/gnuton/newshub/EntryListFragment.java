package com.gnuton.newshub;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.tasks.DownloadWebTask;
import com.gnuton.newshub.tasks.RSSParseTask;
import com.gnuton.newshub.types.RSSFeed;
import com.gnuton.newshub.utils.Notifications;

import java.util.List;

/**
 * Created by gnuton on 5/18/13.
 */
public class EntryListFragment extends Fragment implements DownloadWebTask.OnRequestCompletedListener, RSSParseTask.OnParsingCompletedListener {
    private static final String TAG = "MY_LIST_FRAGMENT";
    private OnItemSelectedListener itemSelectedListener;
    private List rssEntries = null;
    private RSSFeed mFeed;

    // Sends data to another fragment trough the activity using an internal interface.
    public interface OnItemSelectedListener {
        public void onItemSelected(RSSEntry entry);
    }

    // Executed when data is downloaded from the internet
    @Override
    public void onRequestCompleted(String buffer) {
        Log.d(TAG, "Got Buffer");
        if (buffer == null) {
            Log.e(TAG, "Unable to download content");
            return;
        }
        // Parse RSS buffer in a separate thread. onParsingCompleted is called when the operation terminates
        new RSSParseTask(this).execute(buffer);
    }

    // Callback executed when parsing is completed
    @Override
    public void onParsingCompleted(final List entries) {
        Log.d(TAG, "Parsing completed");

        Context context = getActivity();
        if (entries == null) {
            CharSequence text = context.getResources().getString(R.string.warning_no_entries_found);
            Notifications.showWarning(text.toString());
            return;
        }
        this.rssEntries = entries;

        // Creates data controller (adapter) for listview abd set "entries" as  data
        EntryListAdapter adapter = new EntryListAdapter(context, R.id.entrylistView, entries);
        ListView listView = (ListView) getView().findViewById(R.id.entrylistView);
        listView.setAdapter(adapter);

        // Define action (open activity) when a list item is selected
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            private final List rssEntries = entries;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                RSSEntry entry = (RSSEntry) entries.get(i);
                itemSelectedListener.onItemSelected(entry);
            }
        });
    }

    // onAttach checks that activity implements itemSelectedListener
    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "ATTACHED");
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            itemSelectedListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement EntryListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "CREATEVIEW");
        View view = inflater.inflate(R.layout.entrylist_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "START");
        // called when fragment is visible
        if (this.rssEntries != null) {
            onParsingCompleted(this.rssEntries);
        }
    }

    private void updateList() {
        Log.d(TAG, "UPDATE");
        if (this.mFeed == null)
            return;
        //String url ="http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
        Context c = getActivity().getApplicationContext();
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            new DownloadWebTask(this).execute(this.mFeed.url);
        } else {
            Log.w(TAG, "Device not connected");
            //TODO display error (use notification API?)
        }

        //String newTime = String.valueOf(System.currentTimeMillis());
    }

    public void setUrl(RSSFeed feed) {
        this.mFeed= feed;
        this.updateList();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DESTROY");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "DETACH");
    }
}
