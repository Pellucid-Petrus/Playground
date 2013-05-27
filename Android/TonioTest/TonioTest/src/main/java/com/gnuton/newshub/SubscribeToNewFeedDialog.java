package com.gnuton.newshub;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import com.gnuton.newshub.types.RSSFeed;

/**
 * Created by gnuton on 5/28/13.
 */
public class SubscribeToNewFeedDialog extends DialogFragment {
    private final String TAG = SubscribeToNewFeedDialog.class.getName();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] providers = new String[] {"1", "2"};
        builder.setTitle("TEST")
                .setItems(providers, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "CLICKED" + which);
                        RSSFeed feed = new RSSFeed(0,"TEST TITLE","TEST URL");
                        mListener.onFeedSelected(feed);
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        return builder.create();
    }
}
