package com.gnuton.newshub;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gnuton.newshub.tasks.ImageGetter;
import com.gnuton.newshub.types.RSSEntry;
import com.gnuton.newshub.tasks.BoilerPipeTask;
import com.gnuton.newshub.adapters.ImageAdapter;

/**
 * Created by gnuton on 5/18/13.
 */
public class ArticleFragment extends Fragment implements BoilerPipeTask.OnBoilerplateRemovedListener {
    private static final String TAG = "ARTICLE_FRAGMENT";
    private RSSEntry mEntry = null;
    private AsyncTask mTask = null;
    private ImageAdapter mImageAdapter;
    private ImageGetter mImageGetter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create view");
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.article_fragment, container, false);

        // Instantiate imageGetter
        final TextView contentView = (TextView) view.findViewById(R.id.ContentTextView);
        mImageGetter = new ImageGetter(contentView);

        // View Pager
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        mImageAdapter = new ImageAdapter(view.getContext());
        mImageGetter.setAdapter(mImageAdapter, viewPager);
        viewPager.setAdapter(mImageAdapter);

        // Define action for button
        final Button readMoreButton = (Button) view.findViewById(R.id.ReadMoreButton);
        readMoreButton.setVisibility(View.GONE);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mEntry != null) {
                    String content = mEntry.content;
                    if (content != null) {
                        Spanned myStringSpanned = Html.fromHtml(content, mImageGetter, null);
                        contentView.setText(myStringSpanned, TextView.BufferType.SPANNABLE);
                        readMoreButton.setVisibility(View.GONE);
                    } else {
                        // Show content in a browser
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mEntry.link));
                        startActivity(i);
                    }
                }
                // scroll up
                ScrollView scrollview = (ScrollView) getView().findViewById(R.id.scrollView);
                scrollview.pageScroll(View.FOCUS_UP);
            }
        });

        // Define action for button
        final Button openLinkButton = (Button) view.findViewById(R.id.OpenLinkButton);
        openLinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mEntry != null) {
                    // Show content in a browser
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(mEntry.link));
                    startActivity(i);
                }
            }

        });

        // Define action for button
        final Button shareLinkButton = (Button) view.findViewById(R.id.ShareLinkButton);
        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mEntry != null) {
                    // Show content in a browser
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, mEntry.link);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.sharing_url_string);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }

        });

        return view;
    }

    @Override
    public void onActivityCreated(android.os.Bundle savedInstanceState) {
        super.onStart();
        Log.d(TAG, "ACTIVITY CREATED");
        // called when fragment is visible
        if (mEntry != null) {
            setEntry(mEntry);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "START");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mTask != null)
            this.mTask.cancel(true);
        Log.d(TAG, "DESTROY");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "DETACH");
    }

    public void setEntry(RSSEntry entry) {
        Log.d(TAG,"Set mEntry");
        if (entry == mEntry)
            return;
        this.mEntry = entry;

        // reset imageAdapter
        if (mImageAdapter != null){
            mImageAdapter.mImages.clear();
            mImageAdapter.notifyDataSetChanged();
        }

        if (getView() == null) {
            return;
        }

        // Hide Read more button
        Button readMoreButton = (Button) getView().findViewById(R.id.ReadMoreButton);
        readMoreButton.setVisibility(View.GONE);

        //Set Title
        TextView titleView = (TextView) getView().findViewById(R.id.TitleTextView);
        Spanned titleSpanned = Html.fromHtml(entry.title, mImageGetter, null);
        titleView.setText(titleSpanned);

        //Set page content
        TextView contentView = (TextView) getView().findViewById(R.id.ContentTextView);
        String content;

        if (entry.content != null) {
            content = entry.content;
        } else {
            content = entry.summary;
            fetchFullArticle(entry);
        }
        Spanned contentSpanned = Html.fromHtml(content, mImageGetter, null);
        contentView.setText(contentSpanned, TextView.BufferType.SPANNABLE);

        // scroll up
        ScrollView scrollview = (ScrollView) getView().findViewById(R.id.scrollView);
        scrollview.pageScroll(View.FOCUS_UP);
    }

    private void fetchFullArticle(RSSEntry entry) {
        this.mTask = new BoilerPipeTask(this).execute(entry);

        /*
        Context c = getActivity().getApplicationContext();
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            //this.mTask = new BoilerPipeTask(this).execute(mEntry.link);
        } else {
            Log.w(TAG, "Device not connected");
            //TODO display error (use notification API?)
        }*/
    }
    /*
    @Override
    public void onBoilerplateRemoved(String buffer) {
        Log.d(TAG, "Page Downloaded");
        View v = getView();
        if (v == null)
            return;
        if (buffer == null || buffer == ""){
            return;
        }

        TextView view = (TextView) v.findViewById(R.id.ContentTextView);
        if (view != null){
            Spanned myStringSpanned = Html.fromHtml(buffer, null, null);
            view.setText(myStringSpanned, TextView.BufferType.SPANNABLE);

        }
        this.mTask = null;
    }*/


    @Override
    public void onBoilerplateRemoved(RSSEntry[] entries) {
        Log.d("TAG", "BOILER PLATE REMOVED");
        RSSEntry e =entries[0];

        if (!this.mEntry.link.equals(e.link))
            return;

        this.mEntry = e;
        Button readMoreButton = (Button) getView().findViewById(R.id.ReadMoreButton);
        readMoreButton.setVisibility(View.VISIBLE);

    }
}

