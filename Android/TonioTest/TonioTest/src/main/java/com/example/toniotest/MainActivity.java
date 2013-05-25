package com.example.toniotest;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.toniotest.types.RSSEntry;
import com.example.toniotest.utils.RSSParseTask;


public class MainActivity extends FragmentActivity implements MyListFragment.OnItemSelectedListener {
    private static final String TAG = "MAIN_ACTIVITY";
    private String[] mItems = {};
    private final String[] mLeftTitles = new String[] {"test1", "test2"};
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mDrawerListAdapter;
    private ListView mDrawerList;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "CREATEEEEEEEE");

        /*Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        Log.d(TAG, "Orientation " + rotation);
        If in portrait mode
        if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_0 )*/

        if (savedInstanceState == null) {
            Fragment listFragment =  new MyListFragment();
            listFragment.setRetainInstance(true);

            Log.d(TAG, "ID_:" + listFragment.getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, listFragment)
                    .commit();
        }

        //Set up Navigation drawer
        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        mDrawerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLeftTitles);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
            // enable ActionBar app icon to behave as action to toggle nav drawer
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.drawer_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        //savedInstanceState.putString("MyString", "Welcome back to Android");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemSelected(RSSEntry e) {
        DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (df != null && df.isInLayout()) {
            df.setEntry(e);
        } else {
            df = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slidein,R.animator.slideout,R.animator.slideinpop, R.animator.slideoutpop)
                    .replace(R.id.container, df)
                    .addToBackStack(null)
                    .commit();
            df.setEntry(e);

            // Fragment not present in layout. Launch Detail activity
            /*Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.ENTRY, e);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                // New activity slides in and old slides out
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.animator.slidein, R.animator.slideout).toBundle();
                startActivity(intent, bndlanimation);
            } else {
                startActivity(intent);
            }*/

        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update selected item and title, then close the drawer
        Log.d(TAG, "Item " + mLeftTitles[position] + "clicked!");
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
