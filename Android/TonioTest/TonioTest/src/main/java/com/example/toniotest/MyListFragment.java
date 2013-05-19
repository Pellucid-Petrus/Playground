package com.example.toniotest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by gnuton on 5/18/13.
 */
public class MyListFragment extends Fragment {
    private static final String TAG = "MY_LIST_FRAGMENT";


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
        String newTime = String.valueOf(System.currentTimeMillis());
        listener.onItemSelected(newTime);
    }
}
