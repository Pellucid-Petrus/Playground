package com.example.toniotest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by gnuton on 5/19/13.
 */
public class DetailActivity extends FragmentActivity{
    public static final String EXTRA_STR = "STR";
    private static final String TAG = "DETAIL_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CREATED");

        // Prevents crash due to missing layout if activity is open and device changes orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String s = extras.getString(EXTRA_STR);
            TextView t = (TextView) findViewById(R.id.textView);
            t.setText(s);
        }
    }
}
