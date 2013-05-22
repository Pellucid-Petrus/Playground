package com.example.toniotest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.toniotest.utils.BoilerPipeTask;
import com.example.toniotest.utils.DownloadWebTask;
import com.example.toniotest.utils.RSSParseTask;

/**
 * Created by gnuton on 5/18/13.
 */
public class DetailFragment extends Fragment implements BoilerPipeTask.OnBoilerplateRemovedListener {
    private static final String TAG = "DETAIL_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        // Created when Fragment needs to crerate its UI
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // code which require context. getActivity().getApplicationContext()
        // method called after onCreateView
    }

    @Override
    public void onStart() {
        super.onStart();
        // called when fragment is visible
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

    public void setEntry(RSSParseTask.Entry entry) {
        //Set Title
        TextView view = (TextView) getView().findViewById(R.id.TitleTextView);
        view.setText(entry.title);

        //Load page
        Context c = getActivity().getApplicationContext();
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            new BoilerPipeTask(this).execute(entry.link);
        } else {
            Log.w(TAG, "Device not connected");
            //TODO display error (use notification API?)
        }
    }

    @Override
    public void onBoilerplateRemoved(String buffer) {
        Log.d(TAG, "Page Downloaded");
        TextView view = (TextView) getView().findViewById(R.id.ContentTextView);
        if (view != null)
            view.setText(buffer);
    }
}
