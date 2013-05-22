package com.example.toniotest;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import com.example.toniotest.utils.RSSParseTask;


public class MainActivity extends FragmentActivity implements MyListFragment.OnItemSelectedListener {
    private static final String TAG = "MAIN_ACTIVITY";
    private String[] items = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "CREATEEEEEEEE");

        // Retain Fragment state (it's not destroyed) across configuration changes
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.listFragment);
        if (f != null) {
            f.setRetainInstance(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemSelected(RSSParseTask.Entry e) {
        DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (df != null && df.isInLayout()) {
            df.setEntry(e);
        } else {
            // Fragment not present in layout. Launch Detail activity
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.ENTRY, e);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                // New activity slides in and old slides out
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.animator.slidein, R.animator.slideout).toBundle();
                startActivity(intent, bndlanimation);
            } else {
                startActivity(intent);
            }

        }
    }
}
