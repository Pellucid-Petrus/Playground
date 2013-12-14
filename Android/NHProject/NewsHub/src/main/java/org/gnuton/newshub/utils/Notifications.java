package org.gnuton.newshub.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * Notification class
 */
public class Notifications {
    /**
     *
     * @param resourceMsg = R.string.something
     */
    static public void showMsg(final int resourceMsg){
        Context context = MyApp.getContext();
        if (context == null)
            return;
        String msg = context.getResources().getString(resourceMsg);
        showMsg(msg);
    }
    static public void showMsg(final String msg){
        if (MyApp.mMainActivity == null)
            return;

        if (MyApp.mMainActivity != null)
            MyApp.mMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = MyApp.getContext();

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, msg, duration);
                    toast.show();
                }
            });
    }

    public static void showErrorMsg(final int resourceMsg) {
        final Context context = MyApp.mMainActivity;
        final String msg = MyApp.getContext().getResources().getString(resourceMsg);
        if (MyApp.mMainActivity != null)
            MyApp.mMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(context).setMessage(msg).setNeutralButton(android.R.string.ok, null).show();
                    EasyTracker easyTracker = EasyTracker.getInstance(context);

                    // MapBuilder.createEvent().build() returns a Map of event fields and values
                    // that are set and sent with the hit.
                    easyTracker.send(MapBuilder
                            .createEvent("error",     // Event category (required)
                                    "error_msg",  // Event action (required)
                                    msg,   // Event label
                                    null)            // Event value
                            .build()
                    );

                }
            });
    }
}
