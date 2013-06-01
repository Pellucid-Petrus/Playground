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
import com.gnuton.newshub.tasks.DownloadWebTask;
import com.gnuton.newshub.types.RSSFeed;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnuton on 5/28/13.
 */
public class Subscribe extends DialogFragment {
    private final String TAG = Subscribe.class.getName();
    private CountDownTimer mSearchTiimer;
    private View mDlgLayout;

    private RSSFeedDataSource mFeedDDataSource;
    private List<RSSFeed> mFeeds;
    private ArrayAdapter < RSSFeed > adapter;

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
        final Subscribe t = this;

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
        mFeedDDataSource = new RSSFeedDataSource(ctx);
        mFeeds = new ArrayList<RSSFeed>();
        adapter = new ArrayAdapter<RSSFeed>(ctx, android.R.layout.simple_list_item_1, mFeeds);
        ListView lv = (ListView) mDlgLayout.findViewById(R.id.subscribe_listView);
        if (lv != null)
            lv.setAdapter(adapter);

        // Add listeners to editText
        EditText et = (EditText) mDlgLayout.findViewById(R.id.subscribe_editText);
        et.addTextChangedListener( new TextWatcher() {
            class mCountDownTimer extends  CountDownTimer implements DownloadWebTask.OnRequestCompletedListener {

                public mCountDownTimer(long millisInFuture, long countDownInterval) {
                    super(millisInFuture, countDownInterval);
                }

                @Override
                public void onRequestCompleted(String buffer) {
                    Log.d(TAG, "Got new providers");
                    mFeeds.clear();

                    try {
                        JSONArray jArray = new JSONArray(buffer);
                        for (int i=0; i< jArray.length(); ++i) {
                            JSONObject j = jArray.getJSONObject(i);
                            String title = j.getString(DbHelper.FEEDS_TITLE);
                            String url = j.getString(DbHelper.FEEDS_URL);
                            RSSFeed f = new RSSFeed(title, url);
                            mFeeds.add(f);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    Log.d(TAG," Start searching");
                    EditText e = (EditText) mDlgLayout.findViewById(R.id.subscribe_editText);

                    // Fetch RSS list from gnuton.org
                    String url = "http://www.gnuton.org/newshub/recomm.php?q=" + e.getText().toString();
                    new DownloadWebTask(this).execute(url);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                int delayBeforeSearching = 3000;

                if (mSearchTiimer != null)
                    mSearchTiimer.cancel();

                mSearchTiimer = new mCountDownTimer(delayBeforeSearching, delayBeforeSearching).start();
            }
        });
        return builder.create();
    }
}
