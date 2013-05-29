package com.gnuton.newshub;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.gnuton.newshub.db.DbHelper;
import com.gnuton.newshub.db.RSSFeedDataSource;
import com.gnuton.newshub.types.RSSFeed;
import android.os.Handler;

import java.util.List;

/**
 * Created by gnuton on 5/28/13.
 */
public class Subscribe extends DialogFragment {
    private final String TAG = Subscribe.class.getName();
    private CountDownTimer mSearchTiimer;
    private View mDlgLayout;

    onDialogListener mListener;

    public interface onDialogListener {
        public void onFeedSelected(RSSFeed feed);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (onDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] providers = new String[] {};

        //Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mDlgLayout = inflater.inflate(R.layout.subscribe_dialog, null);
        builder.setView(mDlgLayout);
        builder.setTitle("TEST");

        /*
        builder.setItems(providers, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "CLICKED" + which);
                RSSFeed feed = new RSSFeed(0, "TEST TITLE", "TEST URL");
                mListener.onFeedSelected(feed);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });*/

        // Binds SQLite to list
        Context ctx = this.getActivity();
        RSSFeedDataSource feedDDataSource = new RSSFeedDataSource(ctx);
        List<RSSFeed> feeds = feedDDataSource.getAll();
        ArrayAdapter < RSSFeed > adapter = new ArrayAdapter<RSSFeed>(ctx, android.R.layout.simple_list_item_1, feeds);
        ListView lv = (ListView) mDlgLayout.findViewById(R.id.subscribe_listView);
        lv.setAdapter(adapter);

        // Add listeners to editText
        EditText et = (EditText) mDlgLayout.findViewById(R.id.subscribe_editText);
        et.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG,"AFTER TEXT CHANGED");
                if (mSearchTiimer != null)
                    mSearchTiimer.cancel();

                mSearchTiimer = new CountDownTimer(3000, 3000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        Log.d(TAG," CHANGED");
                        EditText e = (EditText) mDlgLayout.findViewById(R.id.subscribe_editText);
                        //e.setText("done!");
                    }
                }.start();

            }
        });
        return builder.create();
    }

    public void addItems(View v) {
        //listItems.add("Clicked : "+clickCounter++);
        //adapter.notifyDataSetChanged();
    }
}
