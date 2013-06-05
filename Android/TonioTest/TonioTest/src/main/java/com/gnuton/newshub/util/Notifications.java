package com.gnuton.newshub.util;

import android.content.Context;
import android.widget.Toast;

import com.gnuton.newshub.R;

/**
 * Created by gnuton on 6/5/13.
 */
public class Notifications {
    static public void showWarning(String msg){
        Context context = MyApp.getContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}
