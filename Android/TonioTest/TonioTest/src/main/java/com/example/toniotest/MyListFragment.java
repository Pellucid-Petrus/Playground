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
import android.widget.Button;
import com.example.toniotest.utils.DownloadWebTask;
import com.example.toniotest.utils.RSSParseTask;

import java.util.List;

/**
 * Created by gnuton on 5/18/13.
 */
public class MyListFragment extends Fragment implements DownloadWebTask.OnRequestCompletedListener, RSSParseTask.OnParsingCompletedListener {
    private static final String TAG = "MY_LIST_FRAGMENT";

    @Override
    public void onRequestCompleted(String buffer) {
        Log.d(TAG, "Got Buffer");
        new RSSParseTask(this).execute(buffer);
    }

    @Override
    public void onParsingCompleted(List list) {
        Log.d(TAG, "Parsing completed");
        //listener.onItemSelected(buffer);
    }

    // Sends data to another fragment trough the activity
    // using an internal interface.
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        public void onItemSelected(String str);
    }

    // onAttach checks that activity implements listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
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
            // display error
            listener.onItemSelected(getResources().getString(R.string.networkConnectionError));
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
