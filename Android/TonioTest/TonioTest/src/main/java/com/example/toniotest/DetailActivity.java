package com.example.toniotest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import com.example.toniotest.utils.RSSParseTask;

/**
 * Created by gnuton on 5/19/13.
 */
public class DetailActivity extends FragmentActivity{
    private static final String TAG = "DETAIL_ACTIVITY";
    public static final String ENTRY = "ENTRY";

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
            RSSParseTask.Entry e = (RSSParseTask.Entry) extras.getSerializable(ENTRY);
            TextView t = (TextView) findViewById(R.id.textView);
            t.setText(e.title);
        }
    }
}
