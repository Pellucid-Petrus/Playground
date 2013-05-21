package com.example.toniotest;

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
import android.widget.Button;
import android.widget.ListView;
import com.example.toniotest.utils.DownloadWebTask;
import com.example.toniotest.utils.RSSParseTask;

import java.util.List;

/**
 * Created by gnuton on 5/18/13.
 */
public class MyListFragment extends Fragment implements DownloadWebTask.OnRequestCompletedListener, RSSParseTask.OnParsingCompletedListener {
    private static final String TAG = "MY_LIST_FRAGMENT";
    private OnItemSelectedListener itemSelectedListener;
    //protected List rssEntries = null;

    // Sends data to another fragment trough the activity using an internal interface.
    public interface OnItemSelectedListener {
        public void onItemSelected(RSSParseTask.Entry entry);
    }

    @Override
    public void onRequestCompleted(final String buffer) {
        Log.d(TAG, "Got Buffer");
        // Parse RSS buffer in a separate thread. onParsingCompleted is called when the operation terminates
        new RSSParseTask(this).execute(buffer);
    }

    @Override
    public void onParsingCompleted(final List entries) {
        Log.d(TAG, "Parsing completed");

        //this.rssEntries = entries;

        // Creates data controller (adapter) for listview abd set "entries" as  data
        MyListAdapter adapter = new MyListAdapter(getActivity().getApplicationContext(), R.id.listView, entries);
        ListView listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Define action (open activity) when a list item is selected
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            private final List rssEntries = entries;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                RSSParseTask.Entry entry = (RSSParseTask.Entry) entries.get(i);
                itemSelectedListener.onItemSelected(entry);
            }
        });
    }

    // onAttach checks that activity implements itemSelectedListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            itemSelectedListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Button updateButton = (Button) view.findViewById(R.id.button);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        };
        updateButton.setOnClickListener(l);
        return view;
    }

    private void updateDetails() {
        Log.d(TAG, "UPDATE");
        String url ="http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

        Context c = getActivity().getApplicationContext();
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            new DownloadWebTask(this).execute(url);
        } else {
            Log.w(TAG, "Device not connected");
            //TODO display error (use notification API?)
        }

        //String newTime = String.valueOf(System.currentTimeMillis());
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
