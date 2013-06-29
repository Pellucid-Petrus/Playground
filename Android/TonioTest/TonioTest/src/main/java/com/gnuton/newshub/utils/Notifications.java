package com.gnuton.newshub.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by gnuton on 6/5/13.
 */
public class Notifications {
    /**
     *
     * @param resourceMsg = R.string.something
     */
    static public void showMsg(int resourceMsg){
        Context context = MyApp.getContext();
        String msg = context.getResources().getString(resourceMsg).toString();
        showMsg(msg);
    }
    static public void showMsg(String msg){
        Context context = MyApp.getContext();

        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}
