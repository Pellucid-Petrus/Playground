package com.example.toniotest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gnuton on 5/18/13.
 */
public class DetailFragment extends Fragment{
    private static final String TAG = "DETAIL_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        // Created when Fragment needs to crerate its UI
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        return view;
    }

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.textView);
        view.setText(item);
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
}
