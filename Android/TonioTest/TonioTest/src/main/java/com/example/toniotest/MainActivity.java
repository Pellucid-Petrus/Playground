package com.example.toniotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;


public class MainActivity extends FragmentActivity implements MyListFragment.OnItemSelectedListener {
    private static final String TAG = "MAIN_ACTIVITY";
    private String[] items = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.populateList();
        Log.i(TAG, "CREATEEEEEEEE");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void populateList() {
        ListView v = (ListView) this.findViewById(R.id.listView);
        String mite;
        mite = this.getResources().getString(R.string.item_str);
        items = new String[] { mite, mite };
    }

    @Override
    public void onItemSelected(String s) {
        DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (df != null && df.isInLayout()) {
          df.setText(s);
        } else {
            // Fragment not present in layout. Launch Detail activity
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_STR, s);
            startActivity(intent);
        }
    }
}
